package ee.wemakesoftware.navigation.service;

import ee.wemakesoftware.navigation.exception.NotEnoughDataForLocating;
import ee.wemakesoftware.navigation.exception.NotSupportedStationException;
import ee.wemakesoftware.navigation.model.MobileStation;
import ee.wemakesoftware.navigation.model.Report;
import ee.wemakesoftware.navigation.pojo.MobileStationPositionDto;
import ee.wemakesoftware.navigation.repository.MobileStationRepository;
import ee.wemakesoftware.navigation.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class MsLocationService {

    public static final String NOT_ENOUGH_DATA_MSG = "Not enough data for positioning";
    public static final String UNKNOWN_STATION_MSG = "Station has been never detected";

    private MobileStationRepository mobileStationRepository;
    private ReportRepository reportRepository;


    public MobileStationPositionDto getPosition(UUID id) {
        Optional<MobileStation> mobileStation = mobileStationRepository.findById(id);
        if (!mobileStation.isPresent()) {
            throw new NotSupportedStationException(UNKNOWN_STATION_MSG);
        }
        List<Report> reports = reportRepository.findTop3ByMobileStationOrderByTimeDesc(mobileStation.get());
        if (reports.size() <= 2) {
            throw new NotEnoughDataForLocating(id, NOT_ENOUGH_DATA_MSG);
        }
        float[] coordinates = calculateCoordinates(reports);
        updateCoordinates(id, coordinates);
        return makeMSPositionDto(coordinates, id);
    }


    // Trilateration algorithm
    public float[] calculateCoordinates(List<Report> reports) {
        Report report = reports.get(0);
        Report report1 = reports.get(1);
        Report report2 = reports.get(2);

        float x1 = report.getBaseStation().getXCoord();
        float x2 = report1.getBaseStation().getXCoord();
        float x3 = report2.getBaseStation().getXCoord();
        float y1 = report.getBaseStation().getYCoord();
        float y2 = report1.getBaseStation().getYCoord();
        float y3 = report2.getBaseStation().getYCoord();
        float r1 = report.getDistance();
        float r2 = report1.getDistance();
        float r3 = report2.getDistance();

        float x1Sq = x1 * x1;
        float x2Sq = x2 * x2;
        float x3Sq = x3 * x3;
        float y1Sq = y1 * y1;
        float y2Sq = y2 * y2;
        float y3Sq = y3 * y3;
        float r1Sq = r1 * r1;
        float r2Sq = r2 * r2;
        float r3Sq = r3 * r3;

        float numeratorY = (x2 - x1) * (x3Sq + y3Sq - r3Sq) + (x1 - x3) * (x2Sq + y2Sq - r2Sq) +
                (x3 - x2) * (x1Sq + y1Sq - r1Sq);
        float denominatorY = 2 * (y3 * (x2 - x1) + y2 * (x1 - x3) + y1 * (x3 - x2));
        float yCoordinate = numeratorY / denominatorY;

        float numeratorX = r2Sq - r1Sq + x1Sq - x2Sq + y1Sq - y2Sq - 2 * (y1 - y2) * yCoordinate;

        float denominatorX = 2 * (x1 - x2);

        float xCoordinate = numeratorX / denominatorX;
        return new float[]{xCoordinate, yCoordinate};
    }


    public MobileStationPositionDto makeMSPositionDto(float[] coordinates, UUID id) {
        MobileStationPositionDto mobileStationPositionDto = new MobileStationPositionDto();
        mobileStationPositionDto.setX(coordinates[0]);
        mobileStationPositionDto.setY(coordinates[1]);
        mobileStationPositionDto.setId(id);
        return mobileStationPositionDto;
    }

    public MobileStation updateCoordinates(UUID id, float[] coordinates) {
        MobileStation mobileStation = new MobileStation();
        mobileStation.setId(id);
        mobileStation.setLastKnownX(coordinates[0]);
        mobileStation.setLastKnownY(coordinates[1]);
        return mobileStationRepository.save(mobileStation);
    }

}

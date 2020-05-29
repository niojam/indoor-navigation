package ee.wemakesoftware.navigation.service;

import ee.wemakesoftware.navigation.TestConfig;
import ee.wemakesoftware.navigation.exception.NotEnoughDataForLocating;
import ee.wemakesoftware.navigation.exception.NotSupportedStationException;
import ee.wemakesoftware.navigation.model.BaseStation;
import ee.wemakesoftware.navigation.model.MobileStation;
import ee.wemakesoftware.navigation.model.Report;
import ee.wemakesoftware.navigation.pojo.MobileStationPositionDto;
import ee.wemakesoftware.navigation.repository.MobileStationRepository;
import ee.wemakesoftware.navigation.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MsLocationService.class})
class MsLocationServiceTest {

    public static final UUID TEST_MOBILE_STATION_ID = UUID.fromString("123e4567-e89b-42d3-a456-556642440000");

    @Autowired
    private MsLocationService msLocationService;

    @MockBean
    private MobileStationRepository mobileStationRepository;

    @MockBean
    private ReportRepository reportRepository;

    private List<Report> reports;
    private Report report;
    private Report report2;
    private Report report3;
    private MobileStation mobileStation;


    @Test
    void getPosition() {
        setUp();
        MobileStationPositionDto mobileStationPositionDto = new MobileStationPositionDto();
        mobileStationPositionDto.setId(TEST_MOBILE_STATION_ID);
        mobileStationPositionDto.setX(8f);
        mobileStationPositionDto.setY(11f);

        Mockito.when(mobileStationRepository.findById(TEST_MOBILE_STATION_ID)).thenReturn(Optional.of(mobileStation));
        Mockito.when(reportRepository.findTop3ByMobileStationOrderByTimeDesc(mobileStation)).thenReturn(reports);


        assertEquals(mobileStationPositionDto, msLocationService.getPosition(TEST_MOBILE_STATION_ID));
    }

    @Test
    void getPositionUnknownBs() {
        UUID id = UUID.randomUUID();
        Mockito.when(mobileStationRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotSupportedStationException.class, () -> {
            msLocationService.getPosition(TEST_MOBILE_STATION_ID);
        });
        assertEquals("Station has been never detected", exception.getMessage());
    }

    @Test
    void getPositionNotEnoughData() {
        setUp();
        Mockito.when(mobileStationRepository.findById(TEST_MOBILE_STATION_ID)).thenReturn(Optional.of(mobileStation));
        Mockito.when(reportRepository.findTop3ByMobileStationOrderByTimeDesc(mobileStation)).thenReturn(reports.subList(0, 1));
        Exception exception = assertThrows(NotEnoughDataForLocating.class, () -> {
            msLocationService.getPosition(TEST_MOBILE_STATION_ID);
        });
        assertEquals("Not enough data for positioning", exception.getMessage());
    }

    @Test
    void calculateCoordinates() {
        setUp();
        float[] expected = {8f, 11f};
        assertArrayEquals(expected, msLocationService.calculateCoordinates(reports));
    }

    @Test
    void testMakeMSPositionDto() {
        MobileStationPositionDto mobileStationPositionDto = new MobileStationPositionDto();
        UUID id = UUID.randomUUID();
        float[] testCord = {1f, 3f};
        mobileStationPositionDto.setX(testCord[0]);
        mobileStationPositionDto.setY(testCord[1]);
        mobileStationPositionDto.setId(id);
        assertEquals(mobileStationPositionDto, msLocationService.makeMSPositionDto(testCord, id));
    }

    @Test
    void testUpdateCoordinates() {
        MobileStation mobileStation = new MobileStation();
        mobileStation.setId(TEST_MOBILE_STATION_ID);
        float[] coords = {1f, 2f};
        mobileStation.setLastKnownX(coords[0]);
        mobileStation.setLastKnownY(coords[1]);

        Mockito.when(mobileStationRepository.save(mobileStation)).thenReturn(mobileStation);

        assertEquals(mobileStation, msLocationService.updateCoordinates(TEST_MOBILE_STATION_ID, coords));
    }

    void setUp() {
        BaseStation baseStation = new BaseStation();
        baseStation.setId(UUID.randomUUID());
        baseStation.setDetectionRadiusInMeters(11f);
        baseStation.setName("A");
        baseStation.setXCoord(4f);
        baseStation.setYCoord(8f);


        BaseStation baseStation2 = new BaseStation();
        baseStation2.setId(UUID.randomUUID());
        baseStation2.setDetectionRadiusInMeters(11f);
        baseStation2.setName("B");
        baseStation2.setXCoord(11f);
        baseStation2.setYCoord(7f);


        BaseStation baseStation3 = new BaseStation();
        baseStation3.setId(UUID.randomUUID());
        baseStation3.setDetectionRadiusInMeters(11f);
        baseStation3.setName("C");
        baseStation3.setXCoord(16f);
        baseStation3.setYCoord(17f);

        mobileStation = new MobileStation();
        mobileStation.setId(TEST_MOBILE_STATION_ID);

        report = new Report(1L, baseStation, mobileStation, 5f, new Timestamp(System.currentTimeMillis()));
        report2 = new Report(2L, baseStation2, mobileStation, 5f, new Timestamp(System.currentTimeMillis()));
        report3 = new Report(3L, baseStation3, mobileStation, 10f, new Timestamp(System.currentTimeMillis()));

        reports = Arrays.asList(report, report2, report3);
    }
}
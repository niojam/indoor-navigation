package ee.wemakesoftware.navigation.service;


import ee.wemakesoftware.navigation.exception.NotSupportedStationException;
import ee.wemakesoftware.navigation.model.BaseStation;
import ee.wemakesoftware.navigation.model.MobileStation;
import ee.wemakesoftware.navigation.model.Report;
import ee.wemakesoftware.navigation.pojo.report.MsDetectionDto;
import ee.wemakesoftware.navigation.pojo.report.ReportDto;
import ee.wemakesoftware.navigation.repository.BaseStationRepository;
import ee.wemakesoftware.navigation.repository.MobileStationRepository;
import ee.wemakesoftware.navigation.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class DetectionService {

    public static final String UNSUPPORTED_BASE_STATION_MSG = "This base station is not registered. You can register new base station on the endpoint '/bs/save'";
    private ReportRepository reportRepository;
    private BaseStationRepository baseStationRepository;
    private MobileStationRepository mobileStationRepository;
    private MobileStationService mobileStationService;


    public ReportDto reportDetection(List<MsDetectionDto> detectedDtos, UUID baseStationId) {
        List<Report> reports = new ArrayList<>();
        detectedDtos.forEach(detectionDto -> reports.add(makeReport(detectionDto, baseStationId)));
        reportRepository.saveAll(reports);
        return convertReportDto(detectedDtos, baseStationId);
    }

    public ReportDto convertReportDto(List<MsDetectionDto> detectedDtos, UUID baseStationId) {
        ReportDto reportDto = new ReportDto();
        reportDto.setBaseStationId(baseStationId);
        reportDto.setMobileStationDetected(detectedDtos);
        return reportDto;
    }

    public Report makeReport(MsDetectionDto detectedDto, UUID baseStationId) {
        Optional<BaseStation> baseStation = baseStationRepository.findById(baseStationId);
        if (!baseStation.isPresent()) {
            throw new NotSupportedStationException(UNSUPPORTED_BASE_STATION_MSG);
        }

        MobileStation mobileStation = mobileStationRepository.findById(detectedDto.getMobileStationId())
                .orElse(mobileStationService.newMobileStationDetected(detectedDto));

        Report report = reportRepository.findByBaseStationAndMobileStation(baseStation.get(), mobileStation)
                .orElse(new Report());

        report.setBaseStation(baseStation.get());
        report.setMobileStation(mobileStation);
        report.setDistance(detectedDto.getDistance());
        report.setTime(detectedDto.getTime());
        return report;
    }


}

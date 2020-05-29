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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ee.wemakesoftware.navigation.service.DetectionService.UNSUPPORTED_BASE_STATION_MSG;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DetectionService.class})
class DetectionServiceTest {

    public static final String TEST_BASE_STATION_UUID = "123e4567-e89b-42d3-a456-556642440000";
    private MsDetectionDto msDetectionDto1;
    private MsDetectionDto msDetectionDto2;
    private MsDetectionDto msDetectionDto3;
    private List<MsDetectionDto> detectionDtos;
    private BaseStation baseStation;

    @Autowired
    private DetectionService detectionService;

    @MockBean
    private MobileStationRepository mobileStationRepository;

    @MockBean
    private BaseStationRepository baseStationRepository;

    @MockBean
    private MobileStationService mobileStationService;

    @MockBean
    private ReportRepository reportRepository;


    @BeforeEach
    void setUp() {
        msDetectionDto1 = new MsDetectionDto(UUID.randomUUID(), 5f, new Timestamp(System.currentTimeMillis()));
        msDetectionDto2 = new MsDetectionDto(UUID.randomUUID(), 10f, new Timestamp(System.currentTimeMillis()));
        msDetectionDto3 = new MsDetectionDto(UUID.randomUUID(), 1f, new Timestamp(System.currentTimeMillis()));
        detectionDtos = Arrays.asList(msDetectionDto1, msDetectionDto2, msDetectionDto3);
        baseStation = new BaseStation("A", 4f, 6f, 10f);
        baseStation.setId(UUID.fromString(TEST_BASE_STATION_UUID));

    }



    @Test
    void testMakeReport() {
        Report report = new Report();
        Mockito.when(baseStationRepository.findById(baseStation.getId())).thenReturn(Optional.of(baseStation));

        MobileStation mobileStation = new MobileStation();
        mobileStation.setId(msDetectionDto1.getMobileStationId());

        Mockito.when(mobileStationRepository.findById(msDetectionDto1.getMobileStationId()))
                .thenReturn(Optional.of(mobileStation));

        Mockito.when(mobileStationService.newMobileStationDetected(msDetectionDto1)).thenReturn(mobileStation);

        report.setBaseStation(baseStation);
        report.setMobileStation(mobileStation);
        report.setDistance(msDetectionDto1.getDistance());
        report.setTime(msDetectionDto1.getTime());
        assertEquals(report, detectionService.makeReport(msDetectionDto1, UUID.fromString(TEST_BASE_STATION_UUID)));
    }

    @Test
    void testMakeReportNonSupportedStation() {
        Mockito.when(baseStationRepository.findById(baseStation.getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotSupportedStationException.class, () -> {
            detectionService.makeReport(msDetectionDto1, UUID.fromString(TEST_BASE_STATION_UUID));
        });
        assertEquals(UNSUPPORTED_BASE_STATION_MSG, exception.getMessage());
    }


    @Test
    void testReportDetection() {
        ReportDto reportDto = new ReportDto();
        reportDto.setBaseStationId(baseStation.getId());
        reportDto.setMobileStationDetected(detectionDtos);
        Mockito.when(baseStationRepository.findById(baseStation.getId())).thenReturn(Optional.of(baseStation));

        assertEquals(reportDto, detectionService.reportDetection(detectionDtos, UUID.fromString(TEST_BASE_STATION_UUID)));
    }


}
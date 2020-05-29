package ee.wemakesoftware.navigation.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.wemakesoftware.navigation.error.ApiError;
import ee.wemakesoftware.navigation.exception.NotSupportedStationException;
import ee.wemakesoftware.navigation.model.BaseStation;
import ee.wemakesoftware.navigation.pojo.BaseStationDto;
import ee.wemakesoftware.navigation.pojo.report.MsDetectionDto;
import ee.wemakesoftware.navigation.pojo.report.ReportDto;
import ee.wemakesoftware.navigation.service.BaseStationService;
import ee.wemakesoftware.navigation.service.DetectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BaseStationController.class)
class BaseStationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BaseStationService baseStationService;

    @MockBean
    private DetectionService detectionService;

    @MockBean
    private CommandLineRunner initStations;

    private static final String NOT_SUPPORTED_STATION_MSG = "This base station is not registered. You can register new base station on the endpoint '/bs/save'";

    private MsDetectionDto msDetectionDto;
    private UUID baseStationId;
    private List<MsDetectionDto> msDetectionDtos;
    private ReportDto reportDto;

    @BeforeEach
    void setUp() {
        UUID mobileStationId = UUID.randomUUID();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        baseStationId = UUID.randomUUID();
        msDetectionDto = new MsDetectionDto();
        msDetectionDto.setMobileStationId(mobileStationId);
        msDetectionDto.setDistance(1f);
        msDetectionDto.setTime(time);

        msDetectionDtos = Collections.singletonList(msDetectionDto);

        reportDto = new ReportDto();
        reportDto.setBaseStationId(baseStationId);
        reportDto.setMobileStationDetected(msDetectionDtos);
    }

    @Test
    void testReportDetectionGoodResponse() throws Exception {
        Mockito.when(detectionService.reportDetection(msDetectionDtos, baseStationId)).thenReturn(reportDto);
        String responseBody = mvc.perform(post("/report/{baseStationId}", baseStationId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(msDetectionDtos)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(reportDto), responseBody);
    }


    @Test
    void testReportDetectionUnknownBaseStation() throws Exception {
        Mockito.when(detectionService.reportDetection(msDetectionDtos, baseStationId))
                .thenThrow(new NotSupportedStationException(NOT_SUPPORTED_STATION_MSG));
        String responseBody = mvc.perform(post("/report/{baseStationId}", baseStationId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(msDetectionDtos)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        ApiError notSupportedStationError = new ApiError(HttpStatus.BAD_REQUEST);
        notSupportedStationError.setMessage(NOT_SUPPORTED_STATION_MSG);

        assertEquals(objectMapper.writeValueAsString(notSupportedStationError), responseBody);
    }


    @Test
    void saveNewStation() throws Exception {
        BaseStationDto baseStationDto = new BaseStationDto();
        baseStationDto.setDetectionRadiusInMeters(10f);
        baseStationDto.setName("A");
        baseStationDto.setId(baseStationId);
        baseStationDto.setXCoord(1f);
        baseStationDto.setYCoord(2f);

        Mockito.when(baseStationService.saveStation(baseStationDto))
                .thenReturn(baseStationDto);

        String responseBody = mvc.perform(post("/bs/save")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(baseStationDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(baseStationDto), responseBody);
    }

}
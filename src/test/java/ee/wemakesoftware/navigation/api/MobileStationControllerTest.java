package ee.wemakesoftware.navigation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.wemakesoftware.navigation.error.ApiError;
import ee.wemakesoftware.navigation.error.LocatingError;
import ee.wemakesoftware.navigation.exception.NotEnoughDataForLocating;
import ee.wemakesoftware.navigation.exception.NotSupportedStationException;
import ee.wemakesoftware.navigation.pojo.MobileStationPositionDto;
import ee.wemakesoftware.navigation.service.MsLocationService;
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

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@ExtendWith(SpringExtension.class)
@WebMvcTest(MobileStationController.class)
class MobileStationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MsLocationService msLocationService;

    @MockBean
    private CommandLineRunner initStations;

    private static final String NOT_ENOUGH_DATA_ERROR_MSG = "Not enough data for positioning";
    private static final String UNKNOWN_STATION_MSG = "Station has been never detected";

    private UUID mobileStationId;

    @BeforeEach
    private void setUp() {
        mobileStationId = UUID.randomUUID();
    }

    @Test
    void testGetLocationGoodResponse() throws Exception {
        MobileStationPositionDto mobileStationPositionDto = new MobileStationPositionDto();
        mobileStationPositionDto.setId(mobileStationId);
        mobileStationPositionDto.setY(1f);
        mobileStationPositionDto.setX(2f);
        Mockito.when(msLocationService.getPosition(mobileStationId))
                .thenReturn(mobileStationPositionDto);
        String responseBody = mvc.perform(get("/location/{id}", mobileStationId)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(mobileStationPositionDto), responseBody);
    }

    @Test
    void testGetLocationNotEnoughData() throws Exception {
        Mockito.when(msLocationService.getPosition(mobileStationId))
                .thenThrow(new NotEnoughDataForLocating(mobileStationId, NOT_ENOUGH_DATA_ERROR_MSG));
        LocatingError locatingError = new LocatingError(HttpStatus.BAD_REQUEST);
        locatingError.setMobileStationId(mobileStationId);
        locatingError.setMessage(NOT_ENOUGH_DATA_ERROR_MSG);
        String responseBody = mvc.perform(get("/location/{id}", mobileStationId)
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(locatingError), responseBody);
    }

    @Test
    void testGetLocationInvalidStation() throws Exception {
        Mockito.when(msLocationService.getPosition(mobileStationId))
                .thenThrow(new NotSupportedStationException(UNKNOWN_STATION_MSG));
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(UNKNOWN_STATION_MSG);
        String responseBody = mvc.perform(get("/location/{id}", mobileStationId)
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(apiError), responseBody);
    }


}
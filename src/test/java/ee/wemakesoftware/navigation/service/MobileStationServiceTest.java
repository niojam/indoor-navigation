package ee.wemakesoftware.navigation.service;

import ee.wemakesoftware.navigation.TestConfig;
import ee.wemakesoftware.navigation.model.BaseStation;
import ee.wemakesoftware.navigation.model.MobileStation;
import ee.wemakesoftware.navigation.pojo.MobileStationPositionDto;
import ee.wemakesoftware.navigation.pojo.report.MsDetectionDto;
import ee.wemakesoftware.navigation.repository.MobileStationRepository;
import ee.wemakesoftware.navigation.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MobileStationService.class, ModelMapper.class})
class MobileStationServiceTest {

    @Autowired
    private MobileStationService mobileStationService;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private MobileStationRepository mobileStationRepository;

    private MsDetectionDto msDetectionDto;
    private MobileStationPositionDto mobileStationPositionDto;
    private MobileStationPositionDto mobileStationPositionDto2;
    private List<MobileStationPositionDto> mobileStationPositionDtos;
    private MobileStation ms1;

    @BeforeEach
    void setUp() {
        msDetectionDto = new MsDetectionDto(UUID.randomUUID(), 5f, new Timestamp(System.currentTimeMillis()));
        mobileStationPositionDto = new MobileStationPositionDto(UUID.randomUUID());
        mobileStationPositionDto2 = new MobileStationPositionDto(UUID.randomUUID());
        mobileStationPositionDtos = Arrays.asList(mobileStationPositionDto, mobileStationPositionDto2);
        ms1 = new MobileStation();
        ms1.setId(mobileStationPositionDto.getId());
    }


    @Test
    void testGetAll() {
        MobileStation ms2 = new MobileStation();
        ms2.setId(mobileStationPositionDto2.getId());
        List<MobileStation> mobileStations = Arrays.asList(ms1, ms2);
        Mockito.when(mobileStationRepository.findAll()).thenReturn(mobileStations);
        assertEquals(mobileStationPositionDtos, mobileStationService.getAll());
    }

    @Test
    void saveStation() {
        Mockito.when(mobileStationRepository.save(ms1)).thenReturn(ms1);
        assertEquals(mobileStationPositionDto, mobileStationService.saveStation(mobileStationPositionDto));
    }

    @Test
    void testConvertToDto() {
        assertEquals(mobileStationPositionDto, mobileStationService.convertToDto(ms1));
    }

    @Test
    void testConvertToEntity() {
        assertEquals(ms1, mobileStationService.convertToEntity(mobileStationPositionDto));
    }

    @Test
    void testNewMobileStationDetected() {
        MobileStation mobileStation = new MobileStation();
        mobileStation.setId(msDetectionDto.getMobileStationId());

        Mockito.when(mobileStationRepository.save(mobileStation)).thenReturn(mobileStation);
        assertEquals(mobileStation, mobileStationService.newMobileStationDetected(msDetectionDto));
    }


}
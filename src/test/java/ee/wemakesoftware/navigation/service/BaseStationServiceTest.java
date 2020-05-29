package ee.wemakesoftware.navigation.service;

import ee.wemakesoftware.navigation.TestConfig;
import ee.wemakesoftware.navigation.model.BaseStation;
import ee.wemakesoftware.navigation.model.MobileStation;
import ee.wemakesoftware.navigation.pojo.BaseStationDto;
import ee.wemakesoftware.navigation.repository.BaseStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BaseStationService.class, ModelMapper.class})
class BaseStationServiceTest {

    @Autowired
    private BaseStationService baseStationService;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private BaseStationRepository baseStationRepository;

    private BaseStationDto baseStationDto;
    private BaseStationDto baseStationDto2;
    private List<BaseStationDto> baseStationDtos;
    private BaseStation baseStation;

    @BeforeEach
    void setUp() {
        baseStationDto = new BaseStationDto(UUID.randomUUID(), "A", 5f, 4f, 10f);
        baseStationDto2 = new BaseStationDto(UUID.randomUUID(), "B", 1f, 2f, 10f);
        ;
        baseStationDtos = Arrays.asList(baseStationDto, baseStationDto2);
        baseStation = new BaseStation();
        baseStation.setId(baseStationDto.getId());
        baseStation.setDetectionRadiusInMeters(baseStationDto.getDetectionRadiusInMeters());
        baseStation.setName(baseStationDto.getName());
        baseStation.setXCoord(baseStationDto.getXCoord());
        baseStation.setYCoord(baseStationDto.getYCoord());
    }


    @Test
    void getAll() {
        BaseStation baseStation1 = new BaseStation(baseStationDto2.getName(),
                baseStationDto2.getXCoord(), baseStationDto2.getYCoord(), baseStationDto2.getDetectionRadiusInMeters());
        baseStation1.setId(baseStationDto2.getId());
        List<BaseStation> baseStations = Arrays.asList(baseStation, baseStation1);
        Mockito.when(baseStationRepository.findAll()).thenReturn(baseStations);
        assertEquals(baseStationDtos, baseStationService.getAll());
    }

    @Test
    void saveStation() {
        Mockito.when(baseStationRepository.save(baseStation)).thenReturn(baseStation);
        assertEquals(baseStationDto, baseStationService.saveStation(baseStationDto));

    }


}
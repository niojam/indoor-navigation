package ee.wemakesoftware.navigation.service;

import ee.wemakesoftware.navigation.model.BaseStation;
import ee.wemakesoftware.navigation.pojo.BaseStationDto;
import ee.wemakesoftware.navigation.repository.BaseStationRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class BaseStationService implements StationService<BaseStationDto> {

    private BaseStationRepository baseStationRepository;
    private ModelMapper modelMapper;


    @Override
    public List<BaseStationDto> getAll() {
        List<BaseStationDto> baseStationsDtos = new ArrayList<>();
        baseStationRepository.findAll().forEach(baseStation -> baseStationsDtos.add(convertToDto(baseStation)));
        return baseStationsDtos;
    }

    @Override
    public BaseStationDto saveStation(BaseStationDto station) {
        return convertToDto(baseStationRepository.save(convertToEntity(station)));
    }


    public BaseStationDto convertToDto(BaseStation baseStation) {
        return modelMapper.map(baseStation, BaseStationDto.class);
    }

    public BaseStation convertToEntity(BaseStationDto baseStationDto) {
        return modelMapper.map(baseStationDto, BaseStation.class);
    }

}

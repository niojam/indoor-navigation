package ee.wemakesoftware.navigation.service;

import ee.wemakesoftware.navigation.model.MobileStation;
import ee.wemakesoftware.navigation.pojo.MobileStationPositionDto;
import ee.wemakesoftware.navigation.pojo.report.MsDetectionDto;
import ee.wemakesoftware.navigation.repository.MobileStationRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Service
public class MobileStationService implements StationService<MobileStationPositionDto> {

    private ModelMapper modelMapper;
    private MobileStationRepository mobileStationRepository;


    @Override
    public Iterable<MobileStationPositionDto> getAll() {
        List<MobileStationPositionDto> mobileStationPositionDtos = new ArrayList<>();
        mobileStationRepository.findAll().forEach(mobileStation -> mobileStationPositionDtos.add(convertToDto(mobileStation)));
        return mobileStationPositionDtos;
    }

    @Override
    public MobileStationPositionDto saveStation(MobileStationPositionDto station) {
        return convertToDto(mobileStationRepository.save(convertToEntity(station)));
    }


    public MobileStationPositionDto convertToDto(MobileStation mobileStation) {
        return modelMapper.map(mobileStation, MobileStationPositionDto.class);
    }

    public MobileStation convertToEntity(MobileStationPositionDto mobileStationPositionDto) {
        return modelMapper.map(mobileStationPositionDto, MobileStation.class);
    }


    public MobileStation newMobileStationDetected(MsDetectionDto msDetectionDto) {
        MobileStation mobileStation = new MobileStation();
        mobileStation.setId(msDetectionDto.getMobileStationId());
        return mobileStationRepository.save(mobileStation);
    }

}

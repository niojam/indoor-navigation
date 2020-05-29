package ee.wemakesoftware.navigation.api;

import ee.wemakesoftware.navigation.pojo.BaseStationDto;
import ee.wemakesoftware.navigation.pojo.report.MsDetectionDto;
import ee.wemakesoftware.navigation.pojo.report.ReportDto;
import ee.wemakesoftware.navigation.service.BaseStationService;
import ee.wemakesoftware.navigation.service.DetectionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class BaseStationController {

    private DetectionService detectionService;
    private BaseStationService baseStationService;

    @PostMapping("/report/{baseStationId}")
    public ReportDto reportDetection(@RequestBody List<MsDetectionDto> detectionDtos, @PathVariable UUID baseStationId) {
        return detectionService.reportDetection(detectionDtos, baseStationId);
    }


    @PostMapping("bs/save")
    public BaseStationDto saveNewStation(@RequestBody BaseStationDto baseStationDto) {
        return baseStationService.saveStation(baseStationDto);
    }

}

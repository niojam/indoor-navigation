package ee.wemakesoftware.navigation.api;

import ee.wemakesoftware.navigation.pojo.MobileStationPositionDto;
import ee.wemakesoftware.navigation.service.MsLocationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class MobileStationController {

    private MsLocationService msLocationService;

    @GetMapping("/location/{id}")
    public MobileStationPositionDto getLocation(@PathVariable UUID id) {
        return msLocationService.getPosition(id);
    }


}

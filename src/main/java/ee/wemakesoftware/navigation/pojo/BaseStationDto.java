package ee.wemakesoftware.navigation.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseStationDto {

    private UUID id;
    private String name;
    private Float xCoord;
    private Float yCoord;
    private Float detectionRadiusInMeters;

}

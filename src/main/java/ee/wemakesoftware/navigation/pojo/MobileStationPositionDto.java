package ee.wemakesoftware.navigation.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobileStationPositionDto {

    private UUID id;
    private Float x;
    private Float y;

    public MobileStationPositionDto(UUID id) {
        this.id = id;
    }

}

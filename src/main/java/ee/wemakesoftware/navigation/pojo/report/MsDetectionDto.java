package ee.wemakesoftware.navigation.pojo.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsDetectionDto {

    private UUID mobileStationId;
    private Float distance;
    private Timestamp time;

}

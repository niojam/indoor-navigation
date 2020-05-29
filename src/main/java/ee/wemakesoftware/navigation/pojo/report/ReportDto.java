package ee.wemakesoftware.navigation.pojo.report;

import lombok.Data;


import java.util.List;
import java.util.UUID;

@Data
public class ReportDto {

    private UUID BaseStationId;
    private List<MsDetectionDto> mobileStationDetected;

}

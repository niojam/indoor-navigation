package ee.wemakesoftware.navigation.model;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BaseStation extends Station {

    private String name;
    private Float xCoord;
    private Float yCoord;
    private Float detectionRadiusInMeters;

}

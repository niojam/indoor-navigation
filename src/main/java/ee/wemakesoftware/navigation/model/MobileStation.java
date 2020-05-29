package ee.wemakesoftware.navigation.model;

import lombok.*;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MobileStation extends Station {

    private Float lastKnownX;
    private Float lastKnownY;

}

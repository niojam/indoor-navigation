package ee.wemakesoftware.navigation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private BaseStation baseStation;
    @ManyToOne
    @JoinColumn(nullable = false)
    private MobileStation mobileStation;

    private Float distance;
    private Timestamp time;

}

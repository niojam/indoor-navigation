package ee.wemakesoftware.navigation.error;

import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Setter
public class LocatingError extends Error {

    private UUID mobileStationId;

    public LocatingError(HttpStatus status) {
        super(status);
    }

}

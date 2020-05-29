package ee.wemakesoftware.navigation.error;

import lombok.Setter;
import org.springframework.http.HttpStatus;


@Setter
public class ApiError extends Error {

    public ApiError(HttpStatus status) {
        super(status);
    }

}

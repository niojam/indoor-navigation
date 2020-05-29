package ee.wemakesoftware.navigation.error;

import ee.wemakesoftware.navigation.exception.NotEnoughDataForLocating;
import ee.wemakesoftware.navigation.exception.NotSupportedStationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotSupportedStationException.class)
    protected ResponseEntity<Object> handleNotSupportedStation(
            NotSupportedStationException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(NotEnoughDataForLocating.class)
    protected ResponseEntity<Object> handleNotEnoughDataForLocating(
            NotEnoughDataForLocating ex) {
        LocatingError locatingError = new LocatingError(BAD_REQUEST);
        locatingError.setMessage(ex.getMessage());
        locatingError.setMobileStationId(ex.getMStationId());
        return buildResponseEntity(locatingError);
    }

    private ResponseEntity<Object> buildResponseEntity(Error error) {
        return new ResponseEntity<>(error, error.getStatus());
    }

}

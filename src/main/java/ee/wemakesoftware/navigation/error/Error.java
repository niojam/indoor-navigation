package ee.wemakesoftware.navigation.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public abstract class Error {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private LocalDateTime timestamp;
    private String message;


    private Error() {
        timestamp = LocalDateTime.now();
    }

    Error(HttpStatus status) {
        this();
        this.status = status;
    }

}

package ee.wemakesoftware.navigation.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class NotEnoughDataForLocating extends RuntimeException {

    private UUID mStationId;

    public NotEnoughDataForLocating(UUID mStationId, String message) {
        super(message);
        this.mStationId = mStationId;
    }

}

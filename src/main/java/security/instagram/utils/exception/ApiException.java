package security.instagram.utils.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode code;
    private final Object details;

    public ApiException(ErrorCode code, String message) {
        super(message);
        this.code = code;
        this.details = null;
    }

    public ApiException(ErrorCode code, String message, Object details) {
        super(message);
        this.code = code;
        this.details = details;
    }
}
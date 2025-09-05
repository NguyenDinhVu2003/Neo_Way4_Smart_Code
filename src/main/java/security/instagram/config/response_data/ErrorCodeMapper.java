package security.instagram.config.response_data;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import security.instagram.utils.exception.InvalidRequestException;

import java.util.HashMap;
import java.util.Map;

public class ErrorCodeMapper {
    private static final Map<Class<? extends Exception>, String> errorCodeMap = new HashMap<>();

    static {
        errorCodeMap.put(IllegalArgumentException.class, "ERR-400");
        errorCodeMap.put(NullPointerException.class, "ERR-5001");
        errorCodeMap.put(RuntimeException.class, "ERR-500");
        errorCodeMap.put(Exception.class, "ERR-500");
        errorCodeMap.put(InvalidRequestException.class, "ERR-400");
        errorCodeMap.put(BadCredentialsException.class, "ERR-400");
    }

    public static String getErrorCode(Exception e, HttpStatus status) {
        return errorCodeMap.getOrDefault(e.getClass(), "ERR-" + status.value());
    }
}

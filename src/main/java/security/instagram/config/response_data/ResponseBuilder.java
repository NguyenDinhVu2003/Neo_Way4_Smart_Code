package security.instagram.config.response_data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public class ResponseBuilder {
    private ResponseBuilder(){

    }
    public static <T> ResponseEntity<ApiResponse<T>> success(
            T data,
            String messageCode,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.<T>builder()
                        .messageCode(messageCode)
                        .path(request.getRequestURI())
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage(HttpStatus.OK.getReasonPhrase())
                        .data(data)
                        .build()
        );
    }

    public static ResponseEntity<ApiResponse<Object>> error(
            String errorCode,
            String messageCode,
            HttpStatus status,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(status).body(
                ApiResponse.builder()
                        .errorCode(errorCode)
                        .messageCode(messageCode)
                        .path(request.getRequestURI())
                        .responseCode(status.value())
                        .responseMessage(status.getReasonPhrase())
                        .build()
        );
    }
}

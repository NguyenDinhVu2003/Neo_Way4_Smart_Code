package security.instagram.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import security.instagram.config.response_data.ApiResponse;
import security.instagram.config.response_data.ErrorCodeMapper;
import security.instagram.utils.exception.InvalidRequestException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (e instanceof IllegalArgumentException || e instanceof InvalidRequestException) {
            status = HttpStatus.BAD_REQUEST;
        }
        String errorCode = ErrorCodeMapper.getErrorCode(e, status);
        return ResponseEntity.status(status).body(
                ApiResponse.builder()
                        .errorCode(errorCode)
                        .messageCode(e.getMessage())
                        .path(request.getRequestURI())
                        .responseCode(status.value())
                        .responseMessage(status.getReasonPhrase())
                        .data(null)
                        .build()
        );
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationServiceException(BadCredentialsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String errorCode = ErrorCodeMapper.getErrorCode(e, status);
        return ResponseEntity.status(status).body(
                ApiResponse.builder()
                        .errorCode(errorCode)
                        .messageCode(e.getClass().getSimpleName())
                        .path(request.getRequestURI())
                        .responseCode(status.value())
                        .responseMessage(status.getReasonPhrase())
                        .data(null)
                        .build()
        );
    }
}

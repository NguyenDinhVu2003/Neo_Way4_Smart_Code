package security.instagram.config.response_data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String messageCode;
    private String errorCode;
    private String path;
    private Integer responseCode;
    private String responseMessage;
    private T data;
}

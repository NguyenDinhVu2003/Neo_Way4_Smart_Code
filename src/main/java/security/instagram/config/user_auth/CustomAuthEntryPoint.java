package security.instagram.config.user_auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import security.instagram.config.response_data.ApiResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .errorCode("ERR-401")
                .messageCode("AUTHENTICATION_FAILED")
                .path(request.getRequestURI())
                .responseCode(HttpServletResponse.SC_UNAUTHORIZED)
                .responseMessage(authException.getMessage())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }
}
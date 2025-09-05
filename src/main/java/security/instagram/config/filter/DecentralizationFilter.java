package security.instagram.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import security.instagram.config.auth.BearerContextHolder;
import security.instagram.config.token.ErrorResponse;
import security.instagram.config.token.TokenUtils;
import security.instagram.dto.MyUserDetails;
import security.instagram.entity.Role;
import security.instagram.service.DecentralizationService;
import security.instagram.utils.Constants;
import security.instagram.utils.exception.InvalidRequestException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Component
public class DecentralizationFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;
    private final DecentralizationService decentralizationService;

    @Autowired
    public DecentralizationFilter(TokenUtils tokenUtils, DecentralizationService decentralizationService) {
        this.tokenUtils = tokenUtils;
        this.decentralizationService = decentralizationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwtToken = request.getHeader(Constants.AUTHORIZATION_HEADER);
            if (jwtToken == null || !jwtToken.startsWith(Constants.TOKEN_PREFIX)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid token");
                return;
            }
            jwtToken = jwtToken.replace(Constants.TOKEN_PREFIX, "");

            Claims claims = tokenUtils.checkValidAccessToken(jwtToken);

            String roleStr = (String) claims.get(Constants.ROLE_CLAIMS_NAME);
            Role role;
            try {
                role = Role.valueOf(roleStr);
            } catch (Exception ex) {
                role = Role.USER;
            }

            String path = request.getRequestURI();
            String method = request.getMethod();

            boolean allowed = decentralizationService.isAllowed(role, path, method);
            if (!allowed && role != Role.ADMIN) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Permission denied");
                return;
            }

            filterChain.doFilter(request, response);
        }
        catch (InvalidRequestException e){
            ErrorResponse errorResponse = new ErrorResponse(e.getTimestamp(), e.getError(), e.getError_description(), e.getHttpStatus());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        } finally{
            BearerContextHolder.clearContext();
        }
    }
}
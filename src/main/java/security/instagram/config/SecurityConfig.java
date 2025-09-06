package security.instagram.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import security.instagram.config.filter.JwtAuthenticationFilter;
import security.instagram.config.token.TokenUtils;
import security.instagram.config.user_auth.CustomAuthEntryPoint;
import security.instagram.config.user_auth.MyUsernamePasswordAuthenticationFilter;

/**
 * @author ket_ein17
 * @Date 5/27/2024
 */

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired private CustomAuthEntryPoint customAuthEntryPoint;
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui/index.html",
            "/v2/api-docs/**",
            "/webjars/**",
            "/actuator/**",
            "/swagger-ui.html"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        MyUsernamePasswordAuthenticationFilter customAuthFilter = new MyUsernamePasswordAuthenticationFilter(tokenUtils);
        customAuthFilter.setAuthenticationManager(authenticationManager);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/swagger-ui/**","/api-docs/**","/v1/auth/**","/v1/chat/**").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(customAuthEntryPoint);
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }
}

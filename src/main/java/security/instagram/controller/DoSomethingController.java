package security.instagram.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.instagram.config.response_data.ResponseBuilder;
import security.instagram.utils.exception.InvalidRequestException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/api")
public class DoSomethingController {
    @GetMapping("/test")
    public ResponseEntity<?> doSomething(HttpServletRequest request) {
        return ResponseBuilder.success("Do Something", "SUCCESS", request);
    }
}

package security.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import security.instagram.config.auth.BearerContext;
import security.instagram.config.auth.BearerContextHolder;
import security.instagram.dto.document.DocumentListResponse;
import security.instagram.dto.document.DocumentResponse;
import security.instagram.entity.User;
import security.instagram.repository.UserRepository;
import security.instagram.service.HomeService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService home;
    private final UserRepository users;

    @GetMapping("/latest")
    public ResponseEntity<?> latest(@RequestParam(defaultValue = "5") @Min(1) @Max(20) int limit) {
        User me = users.findByUsername(BearerContextHolder.getContext().getEmail()).orElseThrow();
        List<DocumentResponse> items = home.latest(me, limit);
        return ResponseEntity.ok().body(new DocumentListResponse(items));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<?> topRated(@RequestParam(defaultValue = "5") @Min(1) @Max(20) int limit) {
        User me = users.findByUsername(BearerContextHolder.getContext().getEmail()).orElseThrow();
        List<DocumentResponse> items = home.topRated(me, limit);
        return ResponseEntity.ok().body(new DocumentListResponse(items));
    }

    @GetMapping("/my-latest")
    public ResponseEntity<?> myLatest(@RequestParam(defaultValue = "5") @Min(1) @Max(20) int limit) {
        User me = users.findByUsername(BearerContextHolder.getContext().getEmail()).orElseThrow();
        List<DocumentResponse> items = home.myLatest(me, limit);
        return ResponseEntity.ok().body(new DocumentListResponse(items));
    }
}
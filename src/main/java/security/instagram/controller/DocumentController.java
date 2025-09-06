package security.instagram.controller;

import security.instagram.config.auth.BearerContextHolder;
import security.instagram.dto.document.DocumentCreateRequest;
import security.instagram.dto.document.DocumentResponse;
import security.instagram.dto.document.DocumentUpdateRequest;
import security.instagram.repository.UserRepository;
import security.instagram.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import security.instagram.entity.User;
import security.instagram.utils.exception.ApiException;
import security.instagram.utils.exception.ErrorCode;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService docs;
    private final UserRepository users;

    @PostMapping
    public ResponseEntity<DocumentResponse> create(@Valid @RequestBody DocumentCreateRequest req) {
        User me = users.findByUsername(BearerContextHolder.getContext().getEmail()).orElseThrow();
        return ResponseEntity.status(201).body(docs.create(req, me));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> get(@PathVariable Long id) {
        User me = users.findByUsername(BearerContextHolder.getContext().getEmail()).orElseThrow();
        return ResponseEntity.ok(docs.get(id, me));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DocumentResponse> update(@PathVariable Long id,
                                                   @RequestBody DocumentUpdateRequest req) {
        User me = users.findByUsername(BearerContextHolder.getContext().getEmail()).orElseThrow();
        return ResponseEntity.ok(docs.update(id, req, me));
    }

    private User currentUser(UserDetails principal) {
        if (principal == null || principal.getUsername() == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Unauthenticated");
        }
        User u = users.findByEmailOrUsername(principal.getUsername());
        if (u == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "User not found");
        }
        return u;
    }
}
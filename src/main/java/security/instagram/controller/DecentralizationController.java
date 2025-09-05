package security.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import security.instagram.dto.DecentralizationDto;
import security.instagram.entity.Decentralization;
import security.instagram.entity.Role;
import security.instagram.service.DecentralizationService;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1/api/decentralizations")
@RequiredArgsConstructor
@Validated
public class DecentralizationController {

    private final DecentralizationService service;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody DecentralizationDto dto) {
        try {
            Decentralization saved = service.addDecentralization(toEntity(dto, null));
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate rule (role, apiPattern, httpMethod) already exists");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody DecentralizationDto dto) {
        try {
            Decentralization updated = service.updateDecentralization(toEntity(dto, id));
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate rule (role, apiPattern, httpMethod) already exists");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteDecentralization(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Decentralization>> listAll() {
        // Simple fetch through repository exposure via service is absent, so call repository indirectly by adding a helper or reuse cache not implemented.
        // For simplicity, expose via a temporary inline approach: extend service? Keeping minimal: we add a method in service would be better.
        // Here we assume service.add/update/delete fill repository; we need read. So quick workaround: create a read method.
        return ResponseEntity.ok(fetchAll());
    }

    private List<Decentralization> fetchAll() {
        // Reflection of repository not accessible; better to add a method in service. For now we add it after adding service modification.
        return service.getAll();
    }

    private Decentralization toEntity(DecentralizationDto dto, Long enforcedId) {
        Role roleEnum;
        try {
            roleEnum = Role.valueOf(dto.getRole().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid role: " + dto.getRole());
        }
        String httpMethodNormalized = dto.getHttpMethod().toUpperCase();
        return Decentralization.builder()
                .id(enforcedId)
                .role(roleEnum)
                .apiPattern(dto.getApiPattern())
                .httpMethod(httpMethodNormalized)
                .build();
    }
}


package security.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import security.instagram.config.auth.BearerContextHolder;
import security.instagram.dto.common.PagedResponse;
import security.instagram.dto.document.DocumentResponse;
import security.instagram.entity.Document;
import security.instagram.entity.User;
import security.instagram.repository.DocumentRepository;
import security.instagram.repository.UserRepository;
import security.instagram.service.HomeService;
import security.instagram.service.PermissionService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class SearchController {
    private final DocumentRepository documents;
    private final UserRepository users;
    private final PermissionService perms;

    @GetMapping("/search")
    public ResponseEntity<PagedResponse<DocumentResponse>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) List<String> tags, // TODO: filter by tags
            @RequestParam(required = false) String groupId,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTo,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        User me = users.findByUsername(BearerContextHolder.getContext().getEmail()).orElseThrow();
        // NOTE: For MVP, we fetch and filter in memory. Replace with Specification for production.
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), Math.min(100, size), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Document> all = new PageImpl<>(documents.findAll()); // naive
        Instant from = createdFrom != null ? Instant.parse(createdFrom) : null;
        Instant to = createdTo != null ? Instant.parse(createdTo) : null;
        List<Document> filtered = all.getContent().stream()
                .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
                .filter(d -> q == null ||  q.isEmpty() || (contains(d.getTitle(), q) || contains(d.getSummary(), q) || contains(d.getDescription(), q)))
                .filter(d -> groupId == null || groupId.isEmpty() || (d.getGroup() != null && d.getGroup().toString().equals(groupId)))
                .filter(d -> from == null || !d.getCreatedAt().isBefore(from))
                .filter(d -> to == null || !d.getCreatedAt().isAfter(to))
                .filter(d -> perms.canView(d, me))
                .sorted((a,b)-> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());

        int fromIdx = pageable.getPageNumber() * pageable.getPageSize();
        int toIdx = Math.min(filtered.size(), fromIdx + pageable.getPageSize());
        List<DocumentResponse> items = filtered.subList(Math.min(fromIdx, filtered.size()), toIdx).stream()
                .map(HomeService.Mapper::toDocResponse)
                .collect(Collectors.toList());

        PagedResponse<DocumentResponse> resp = PagedResponse.<DocumentResponse>builder()
                .items(items)
                .page(pageable.getPageNumber() + 1)
                .size(pageable.getPageSize())
                .total(filtered.size())
                .totalPages((int) Math.ceil((double) filtered.size() / pageable.getPageSize()))
                .build();
        return ResponseEntity.ok(resp);
    }

    private boolean contains(String s, String q) {
        return s != null && s.toLowerCase().contains(q.toLowerCase());
    }
}
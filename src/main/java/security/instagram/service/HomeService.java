package security.instagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import security.instagram.dto.document.DocumentResponse;
import security.instagram.entity.Document;
import security.instagram.entity.User;
import security.instagram.repository.DocumentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final DocumentRepository documents;
    private final PermissionService perms;

    public List<DocumentResponse> latest(User me, int limit) {
        return documents.findByIsDeletedFalseOrderByCreatedAtDesc(PageRequest.of(0, limit))
                .stream().filter(d -> perms.canView(d, me))
                .map(Mapper::toDocResponse)
                .collect(Collectors.toList());
    }

    public List<DocumentResponse> topRated(User me, int limit) {
        return documents.findByIsDeletedFalseOrderByAvgRatingDescRatingCountDesc(PageRequest.of(0, limit))
                .stream().filter(d -> perms.canView(d, me))
                .map(Mapper::toDocResponse)
                .collect(Collectors.toList());
    }

    public List<DocumentResponse> myLatest(User me, int limit) {
        return documents.findByOwnerAndIsDeletedFalseOrderByCreatedAtDesc(me, PageRequest.of(0, limit))
                .stream().map(Mapper::toDocResponse).collect(Collectors.toList());
    }

    // Minimal mapper to avoid extra lib
    public static class Mapper {
        public static DocumentResponse toDocResponse(Document d) {
            return DocumentResponse.builder()
                    .id(d.getId().toString())
                    .title(d.getTitle())
                    .summary(d.getSummary())
                    .description(d.getDescription())
                    .tags(d.getTags().stream().map(t -> t.getName()).collect(Collectors.toList()))
                    .visibility(d.getVisibility().name())
                    .groupId(d.getGroup() != null ? d.getGroup().getId().toString() : null)
                    .owner(new DocumentResponse.Owner(d.getOwner().getId().toString(), d.getOwner().getName()))
                    .file(new DocumentResponse.File(null, null, d.getFileType().name().toLowerCase(), d.getFileSizeBytes()))
                    .avgRating(d.getAvgRating())
                    .ratingCount(d.getRatingCount())
                    .aiSummaryStatus(d.getAiSummaryStatus() != null ? d.getAiSummaryStatus().name().toLowerCase() : null)
                    .createdAt(d.getCreatedAt())
                    .updatedAt(d.getUpdatedAt())
                    .build();
        }
    }
}
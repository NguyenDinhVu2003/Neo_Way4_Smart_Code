package security.instagram.dto.document;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentResponse {
    private String id;
    private String title;
    private String summary;
    private String description;
    private List<String> tags;
    private String visibility;
    private String groupId;
    private Owner owner;
    private File file;
    private Double avgRating;
    private Integer ratingCount;
    private String aiSummaryStatus;
    private Instant createdAt;
    private Instant updatedAt;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Owner {
        private String id;
        private String name;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class File {
        private String previewUrl;
        private String downloadUrl;
        private String type;
        private long size;
    }
}
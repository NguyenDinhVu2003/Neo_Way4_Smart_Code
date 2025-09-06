package security.instagram.dto.document;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class DocumentUpdateRequest {
    private String title;
    private String summary;
    private String description;
    private List<String> tags;
    private String visibility; // PRIVATE|GROUP|PUBLIC
    private String groupId;
}
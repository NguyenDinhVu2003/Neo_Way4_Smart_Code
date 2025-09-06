package security.instagram.dto.document;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Getter @Setter
public class DocumentCreateRequest {
    @NotBlank
    @Size(max = 256)
    private String title;

    @Size(max = 4000)
    private String description;

    @Size(max = 4000)
    private String summary;

    private List<@Size(max = 64) String> tags;

    @NotBlank
    private String visibility; // PRIVATE | GROUP | PUBLIC

    private String groupId; // required if visibility = GROUP

    @NotBlank
    private String fileKey;

    @NotBlank
    private String fileType; // pdf | doc | image

    @Positive
    private long fileSizeBytes;

    private AiOptions ai;

    @Getter @Setter
    public static class AiOptions {
        private boolean autoSummarize;
        private boolean autoTagging;
    }
}
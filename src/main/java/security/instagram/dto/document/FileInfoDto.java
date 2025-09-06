package security.instagram.dto.document;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FileInfoDto {
    private String key;
    private String type;
    private long size;
}
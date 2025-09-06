package security.instagram.dto.common;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PagedResponse<T> {
    private List<T> items;
    private int page;
    private int size;
    private long total;
    private int totalPages;
}
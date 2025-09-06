package security.instagram.dto.document;

import security.instagram.dto.document.DocumentResponse;

import java.util.List;

public class DocumentListResponse {
    public final List<DocumentResponse> items;
    public DocumentListResponse(List<DocumentResponse> items) {
        this.items = items;
    }
}
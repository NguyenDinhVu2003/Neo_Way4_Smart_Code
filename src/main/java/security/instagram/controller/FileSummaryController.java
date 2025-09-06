package security.instagram.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import security.instagram.service.FileSummaryService;

@RestController
@RequestMapping("/v1/file")
@RequiredArgsConstructor
@Slf4j
public class FileSummaryController {

    private final FileSummaryService fileSummaryService;

    @PostMapping(value = "/summarize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> summarizeFile(@RequestParam("file") MultipartFile file) {

        // Validate file
        if (file.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body("File không được để trống"));
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return Mono.just(ResponseEntity.badRequest().body("Tên file không hợp lệ"));
        }

        // Check supported file types
        String fileExtension = getFileExtension(fileName).toLowerCase();
        if (!isValidFileType(fileExtension)) {
            return Mono.just(ResponseEntity.badRequest()
                    .body("Định dạng file không được hỗ trợ. Chỉ hỗ trợ: PDF, DOC, DOCX"));
        }

        // Check file size (max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            return Mono.just(ResponseEntity.badRequest()
                    .body("File quá lớn. Kích thước tối đa: 10MB"));
        }

        log.info("Processing file: {} ({})", fileName, formatFileSize(file.getSize()));

        return fileSummaryService.summarizeFile(file)
                .map(summary -> ResponseEntity.ok(summary))
                .onErrorResume(error -> {
                    log.error("Error summarizing file: {}", error.getMessage());
                    return Mono.just(ResponseEntity.internalServerError()
                            .body("Lỗi khi tóm tắt file: " + error.getMessage()));
                });
    }

    private boolean isValidFileType(String extension) {
        return "pdf".equals(extension) || "doc".equals(extension) || "docx".equals(extension);
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }

    private String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
        return String.format("%.1f MB", size / (1024.0 * 1024.0));
    }
}

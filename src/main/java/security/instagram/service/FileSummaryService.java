package security.instagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileSummaryService {

    private final OllamaService ollamaService;

    public Mono<String> summarizeFile(MultipartFile file) {
        try {
            String fileContent = extractTextFromFile(file);

            if (fileContent.trim().isEmpty()) {
                return Mono.just("Không thể đọc nội dung từ file này.");
            }

            // Tạo prompt cho chatbot để tóm tắt
            String prompt = "Hãy tóm tắt nội dung sau đây một cách ngắn gọn và súc tích nhất có thể:\n\n" + fileContent;

            // Gọi chatbot API để tóm tắt
            return ollamaService.streamChat(prompt, null)
                    .reduce("", (accumulated, newContent) -> {
                        if (accumulated.isEmpty()) {
                            return newContent;
                        }
                        return accumulated + " " + newContent;
                    });

        } catch (Exception e) {
            log.error("Error processing file: {}", e.getMessage());
            return Mono.just("Lỗi khi xử lý file: " + e.getMessage());
        }
    }

    private String extractTextFromFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException("Tên file không hợp lệ");
        }

        String fileExtension = getFileExtension(fileName).toLowerCase();
        InputStream inputStream = file.getInputStream();

        switch (fileExtension) {
            case "pdf":
                return extractFromPdf(inputStream);
            case "doc":
                return extractFromDoc(inputStream);
            case "docx":
                return extractFromDocx(inputStream);
            default:
                throw new IllegalArgumentException("Định dạng file không được hỗ trợ: " + fileExtension);
        }
    }

    private String extractFromPdf(InputStream inputStream) throws IOException {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractFromDoc(InputStream inputStream) throws IOException {
        try (HWPFDocument document = new HWPFDocument(inputStream);
             WordExtractor extractor = new WordExtractor(document)) {
            return extractor.getText();
        }
    }

    private String extractFromDocx(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }
}

package security.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import security.instagram.config.response_data.ResponseBuilder;
import security.instagram.service.GoogleDriverService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/v1/documents")
public class DocumentUploadController {
    @Autowired
    GoogleDriverService driverService;
//    @PostMapping("/uploadToGoogleDrive")
//    public ResponseEntity<?> handleFileUpload(@RequestParam("image") MultipartFile multipartFile, HttpServletRequest request) throws IOException, GeneralSecurityException {
//        File tempFile = File.createTempFile("upload-", multipartFile.getOriginalFilename());
//        multipartFile.transferTo(tempFile);
//
//        String mimeType = multipartFile.getContentType();
//        driverService.uploadFile(tempFile, mimeType, "1lGakOeCnBMwEFKkrrbivHg86RMQ39OV3");
//
//        tempFile.delete();
//        return ResponseBuilder.success("Success","200", request);
//    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> handleFileUploadLocal(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) throws IOException, GeneralSecurityException {
        driverService.saveFile(multipartFile);
        return ResponseBuilder.success("Success","200", request);
    }
}

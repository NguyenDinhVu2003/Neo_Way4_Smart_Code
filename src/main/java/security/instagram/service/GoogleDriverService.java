package security.instagram.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class GoogleDriverService {

    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACOUNT_KEY_PATH = getPathToGoodleCredentials();

    private static String getPathToGoodleCredentials() {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "cred.json");
        return filePath.toString();
    }

    private static final String UPLOAD_DIR = "uploads/";
    public String saveFile(MultipartFile multipartFile) throws IOException {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = dir.getAbsolutePath() + "/"  + multipartFile.getOriginalFilename();
        File file = new File(filePath);
        multipartFile.transferTo(file);

        return file.getAbsolutePath(); // returns full system path
    }

//    public String uploadFile(java.io.File file, String mimeType, String parentFolderId) throws IOException {
//        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
//        fileMetadata.setName(file.getName());
//
//        if (parentFolderId != null) {
//            fileMetadata.setParents(Collections.singletonList(parentFolderId));
//        }
//
//        FileContent mediaContent = new FileContent(mimeType, file);
//
//        com.google.api.services.drive.model.File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
////                .setSupportsAllDrives(true)
//                .setFields("id")
//                .execute();
//
//        return uploadedFile.getWebViewLink();
//    }
//
//
//    public void uploadImageToDrive(File file) throws GeneralSecurityException, IOException {
//
//
//        try{
//            String folderId = "1eW70gnMcvPmJJzaPVtSg3CjUSZeK2x21";
//            Drive drive = createDriveService();
//            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
//            fileMetaData.setName(file.getName());
//            fileMetaData.setParents(Collections.singletonList(folderId));
////            FileContent mediaContent = new FileContent(file.t, file);
//            String contentType = java.nio.file.Files.probeContentType(file.toPath());
//            if (contentType == null) {
//                contentType = "application/octet-stream";
//            }
//            FileContent mediaContent = new FileContent(contentType, file);
//
//            com.google.api.services.drive.model.File uploadedFile = drive.files().create(fileMetaData, mediaContent)
//                    .setFields("id").execute();
//            String imageUrl = "https://drive.google.com/uc?export=view&id="+uploadedFile.getId();
//            System.out.println("IMAGE URL: " + imageUrl);
//            file.delete();
//
//        }catch (Exception e){
//
//        }
//
//
//    }
//
////    private Drive createDriveService() throws GeneralSecurityException, IOException {
////
////        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACOUNT_KEY_PATH))
////                .createScoped(Collections.singleton(DriveScopes.DRIVE));
////
////        return new Drive.Builder(
////                GoogleNetHttpTransport.newTrustedTransport(),
////                JSON_FACTORY,
////                credential)
////                .build();
////
////    }
//
//    private Drive createDriveService() throws GeneralSecurityException, IOException {
//        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACOUNT_KEY_PATH))
//                .createScoped(Collections.singleton(DriveScopes.DRIVE));
//
//        return new Drive.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JSON_FACTORY,
//                credential)
//                .setApplicationName("InstagramSpringBootApp") // Set your app name here
//                .build();
//    }

}

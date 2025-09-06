//package security.instagram.config;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
//import com.google.auth.http.HttpCredentialsAdapter;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.auth.oauth2.ServiceAccountCredentials;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.util.Collections;
//
//@Configuration
//public class GoogleDriveConfig {
//
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//
////    @Bean
////    public Drive googleDriveService() throws Exception {
////        InputStream serviceAccountStream = getClass().getResourceAsStream("/cred.json");
////
////        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(serviceAccountStream)
////                .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));
////
////        return new Drive.Builder(
////                GoogleNetHttpTransport.newTrustedTransport(),
////                JSON_FACTORY,
////                new HttpCredentialsAdapter(credentials)
////        ).setApplicationName("NeoWay4App").build();
////    }
//
//    @Bean
//    public Drive googleDriveService() throws Exception {
//        File file = new File("D:/Worldspace/NeoWay4/Neo_Way4_Smart_Code/cred.json");
//
//        try (InputStream serviceAccountStream = new FileInputStream(file)) {
//            GoogleCredentials credentials =  ServiceAccountCredentials.fromStream(serviceAccountStream)
//                    .createScoped(Collections.singleton(DriveScopes.DRIVE));
//
//            GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("D:/Worldspace/NeoWay4/Neo_Way4_Smart_Code/cred.json"))
//                    .createScoped(Collections.singleton(DriveScopes.DRIVE));
//
//            return new Drive.Builder(
//                    GoogleNetHttpTransport.newTrustedTransport(),
//                    JSON_FACTORY,
//                    credential
//            ).setApplicationName("NeoWay4App").build();
//        }
//    }
//}

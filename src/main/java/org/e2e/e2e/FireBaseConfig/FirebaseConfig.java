package org.e2e.e2e.FireBaseConfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        // Cambia esta ruta para apuntar a tu archivo JSON de servicio
        FileInputStream serviceAccount = new FileInputStream("proyecto1-721e7-firebase-adminsdk-mkaih-e43e2d47a3.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        // Inicializa la aplicación Firebase y la retorna
        return FirebaseApp.initializeApp(options);
    }
}

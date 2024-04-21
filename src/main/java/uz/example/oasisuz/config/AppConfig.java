package uz.example.oasisuz.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier.Builder;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;


@Configuration
@EnableConfigurationProperties
public class AppConfig {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public GoogleIdTokenVerifier googleTokenVerifier() {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        Builder verifierBuilder = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID));
        return verifierBuilder.build();
    }
}

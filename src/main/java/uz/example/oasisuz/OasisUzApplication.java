package uz.example.oasisuz;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class OasisUzApplication {

    public static void main(String[] args) {
        SpringApplication.run(OasisUzApplication.class, args);
    }

}

package uz.example.oasisuz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Token Data Transfer Object")
public class TokenDto {
    private String accessToken;

    private String refreshToken;
}

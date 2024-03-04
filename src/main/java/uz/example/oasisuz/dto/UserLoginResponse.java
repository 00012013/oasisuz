package uz.example.oasisuz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginResponse {
    private String accessToken;

    private String refreshToken;

    private String fullName;

    private Integer userId;
}

package uz.example.oasisuz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import uz.example.oasisuz.entity.enums.RoleEnum;

@Data
@AllArgsConstructor
public class UserLoginResponse {
    private String accessToken;

    private String refreshToken;

    private String fullName;

    private Integer userId;

    private RoleEnum role;
}

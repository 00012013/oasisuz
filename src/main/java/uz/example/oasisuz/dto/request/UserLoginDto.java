package uz.example.oasisuz.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;


@Data
@Schema(name = "User Login Request", description = "User Login Request")
@Validated
public class UserLoginDto {
    @NotNull(message = "Email number must not be empty")
    private String email;

    @NotNull(message = "Password must not be empty")
    private String password;
}

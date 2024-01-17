package uz.example.oasisuz.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;

    private String fullName;

    private String phoneNumber;

    private String password;

    private String email;

    private Integer noAdds;
}

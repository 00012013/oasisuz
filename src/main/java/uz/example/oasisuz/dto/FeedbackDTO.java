package uz.example.oasisuz.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {

    private String fullName;
    private String phoneNumber;
    private String message;

}

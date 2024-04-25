package uz.example.oasisuz.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MessageDTO {

    private Integer senderUserId;
    private Integer receiverUserId;
    private String content;
    private Timestamp createdAt;

}

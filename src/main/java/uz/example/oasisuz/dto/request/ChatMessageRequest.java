package uz.example.oasisuz.dto.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChatMessageRequest {
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private Integer chatRoomId;
    private Timestamp createdAt;
}

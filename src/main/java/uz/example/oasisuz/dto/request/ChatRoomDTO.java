package uz.example.oasisuz.dto.request;

import lombok.Data;

@Data
public class ChatRoomDTO {

    private Integer id;
    private ChatMessageRequest lastMessage;

}

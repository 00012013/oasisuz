package uz.example.oasisuz.dto.response;

import lombok.Data;
import uz.example.oasisuz.dto.UsersDto;

import java.util.List;

@Data
public class ChatRoomResponseDTO {
    private Integer id;

    private List<UsersDto> users;

    private MessageDTO lastMessage;
}

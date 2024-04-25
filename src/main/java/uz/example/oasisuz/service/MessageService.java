package uz.example.oasisuz.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.example.oasisuz.dto.request.ChatMessageRequest;
import uz.example.oasisuz.dto.response.ChatRoomResponseDTO;
import uz.example.oasisuz.dto.response.MessageDTO;
import uz.example.oasisuz.entity.ChatRoom;
import uz.example.oasisuz.entity.Message;
import uz.example.oasisuz.entity.Users;
import uz.example.oasisuz.repository.ChatRoomRepository;
import uz.example.oasisuz.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
//    private final SimpMessagingTemplate messagingTemplate;

    private final MessageRepository messageRepository;

    private final ChatRoomRepository chatRoomRepository;
    private final ModelMapper modelMapper;
    private final UsersService usersService;

    public void sendMessageUpdates() {
        // Retrieve messages from the database
        List<Message> messages = messageRepository.findAll();

        // Convert messages to JSON and send to WebSocket clients
//        messagingTemplate.convertAndSend("/topic/messages", messages);
    }

    public List<ChatRoomResponseDTO> getUsersChat(Integer userId) {

        Users user = usersService.getUser(userId);

        List<ChatRoom> allByUsersContains = chatRoomRepository.findAllByUsersContaining(user);
        return allByUsersContains.stream().map(chatRoom -> modelMapper.map(chatRoom, ChatRoomResponseDTO.class)).toList();

    }

    public void sendMessage(ChatMessageRequest message) {
        Users senderUser = usersService.getUser(message.getSenderId());
        Users receiverUser = usersService.getUser(message.getReceiverId());
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(message.getChatRoomId());
        ChatRoom chatRoom;
        if (optionalChatRoom.isEmpty()) {
            ChatRoom newChatRoom = ChatRoom.builder()
                    .users(List.of(senderUser, receiverUser))
                    .build();
            chatRoom = chatRoomRepository.save(newChatRoom);
        } else {
            chatRoom = optionalChatRoom.get();
        }
        Message newMessage = Message.builder()
                .senderUserId(senderUser.getId())
                .receiverUserId(receiverUser.getId())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .chatRoom(chatRoom)
                .build();
        Message savedMessage = messageRepository.save(newMessage);
        chatRoom.setLastMessage(savedMessage);
        chatRoomRepository.save(chatRoom);

    }

    public List<MessageDTO> getChatHistory(Integer chatRoomId) {
        List<Message> chatHistory = messageRepository.findAllByChatRoomId(chatRoomId);
        return chatHistory.stream().map(message -> modelMapper.map(message, MessageDTO.class)).toList();
    }
}

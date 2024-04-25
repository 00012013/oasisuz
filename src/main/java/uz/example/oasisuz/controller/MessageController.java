package uz.example.oasisuz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import uz.example.oasisuz.dto.request.ChatMessageRequest;
import uz.example.oasisuz.dto.response.ChatRoomResponseDTO;
import uz.example.oasisuz.dto.response.MessageDTO;
import uz.example.oasisuz.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/chat/")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;


    @MessageMapping("/message")
    @SendTo("/topic/chat")
    public ChatMessageRequest send(ChatMessageRequest message) {
        return message;
    }

    @GetMapping("get/chat-users/{userId}")
    public List<ChatRoomResponseDTO> getUsersChat(@PathVariable Integer userId) {
        return messageService.getUsersChat(userId);
    }

    @PostMapping("send-message")
    public void sendMessage(@RequestBody ChatMessageRequest message) {
        messageService.sendMessage(message);
    }

    @GetMapping("get/chat-history/{chatRoomId}")
        public List<MessageDTO> getChatHistory(@PathVariable Integer chatRoomId) {
        return messageService.getChatHistory(chatRoomId);
    }
}


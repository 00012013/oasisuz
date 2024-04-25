package uz.example.oasisuz.entity;

import jakarta.persistence.*;
import jdk.jfr.Timespan;
import lombok.*;


import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany
    @JoinTable(
            name = "user_chatroom",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Users> users;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "last_message_id", referencedColumnName = "id")
    private Message lastMessage;

    private int unreadCount;
}

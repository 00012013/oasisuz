package uz.example.oasisuz.entity;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_id_seq")
    @SequenceGenerator(name = "message_id_seq", sequenceName = "message_seq")
    private Integer id;

    @OneToOne
    private ChatRoom chatRoom;

    private Integer senderUserId;

    private Integer receiverUserId;

    private String content;

    private Timestamp createdAt;
}

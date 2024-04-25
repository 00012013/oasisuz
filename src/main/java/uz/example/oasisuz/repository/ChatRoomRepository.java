package uz.example.oasisuz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.example.oasisuz.entity.ChatRoom;
import uz.example.oasisuz.entity.Users;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Integer> {
    List<ChatRoom> findAllByUsersContaining(Users user);
}

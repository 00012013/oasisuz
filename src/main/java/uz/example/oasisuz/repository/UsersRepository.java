package uz.example.oasisuz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.example.oasisuz.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
}

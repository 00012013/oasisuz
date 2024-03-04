package uz.example.oasisuz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.example.oasisuz.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}

package uz.example.oasisuz.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.example.oasisuz.entity.Attachment;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Integer> {
    Optional<Attachment> findByCottageId(Integer product_id);
}

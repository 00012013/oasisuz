package uz.example.oasisuz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.example.oasisuz.entity.Cottage;

@Repository
public interface CottageRepository extends JpaRepository<Cottage,Integer> {
}

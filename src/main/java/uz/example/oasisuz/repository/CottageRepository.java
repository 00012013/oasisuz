package uz.example.oasisuz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.example.oasisuz.entity.Cottage;

import java.util.List;

@Repository
public interface CottageRepository extends JpaRepository<Cottage,Integer> {
    @Query(value = "select * from cottage limit 3", nativeQuery = true)
    List<Cottage> getFistThree();
}

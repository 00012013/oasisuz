package uz.example.oasisuz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.example.oasisuz.entity.Cottage;

import java.util.List;

@Repository
public interface CottageRepository extends JpaRepository<Cottage,Integer> {
    @Query(value = "select * from cottage limit 3", nativeQuery = true)
    List<Cottage> getFistThree();

    @Query(value = "SELECT * FROM cottage WHERE LOWER(name) LIKE CONCAT(LOWER(:name), '%')", nativeQuery = true)
    List<Cottage> searchByName(@Param("name") String name);

    @Query(value = "SELECT * FROM filter(:equipments,:sortBy, :maxPrice, :minPrice, :name)", nativeQuery = true)
    List<Cottage> filterCottage(@Param("equipments") String[] equipments, @Param("sortBy") String sortBy,@Param("maxPrice") double maxPrice, @Param("minPrice") double minPrice, @Param("name") String name);
}

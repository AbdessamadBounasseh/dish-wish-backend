package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uit.ensak.dishwishbackend.model.Chef;

import java.util.List;

@Repository
public interface ChefRepository extends JpaRepository<Chef, Long> {
    List<Chef> findByFirstNameContaining(String query);
    List<Chef> findByLastNameContaining(String query);
}

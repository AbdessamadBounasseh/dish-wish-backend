package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.ensak.dishwishbackend.model.Diet;

import java.util.Optional;

public interface DietRepository extends JpaRepository<Diet, Long> {
    Optional<Diet> findByTitle(String title);
}

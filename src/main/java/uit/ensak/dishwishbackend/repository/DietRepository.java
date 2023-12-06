package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.ensak.dishwishbackend.model.Diet;

public interface DietRepository extends JpaRepository<Diet, Long> {
}

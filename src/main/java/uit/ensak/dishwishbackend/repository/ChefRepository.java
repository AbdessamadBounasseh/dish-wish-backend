package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.ensak.dishwishbackend.model.Chef;

public interface ChefRepository extends JpaRepository<Chef, Long> {
}

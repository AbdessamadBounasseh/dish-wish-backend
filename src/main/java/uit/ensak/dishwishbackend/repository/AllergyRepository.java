package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.ensak.dishwishbackend.model.Allergy;

public interface AllergyRepository extends JpaRepository<Allergy, Long> {
}

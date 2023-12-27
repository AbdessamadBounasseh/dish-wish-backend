package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.ensak.dishwishbackend.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}

package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Rating;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByClientAndType(Client client, String type);
    List<Rating> findByChefAndType(Chef chef, String type);
}

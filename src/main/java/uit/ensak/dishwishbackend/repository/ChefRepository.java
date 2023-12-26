package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Role;

import java.util.List;

@Repository
public interface ChefRepository extends JpaRepository<Chef, Long> {
    Chef findByIdAndRole(Long id, Role role);
}

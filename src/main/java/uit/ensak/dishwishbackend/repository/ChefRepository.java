package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Role;

import java.util.List;

@Repository
public interface ChefRepository extends JpaRepository<Chef, Long> {
    List<Chef> findByFirstNameContaining(String query);
    List<Chef> findByLastNameContaining(String query);
    List<Chef> findByAddressContaining(String query);
    Chef findByIdAndRole(Long id, Role role);
}

package uit.ensak.dishwishbackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Role;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    /*@Query("SELECT c FROM Client c WHERE c.id = :id AND c.role = 'CHEF'")
    Client findChefById(@Param("id") Long id);*/

    Client findByIdAndRole(Long id, Role role);

    Optional<Client> findClientByEmail(String email);
    @Transactional
    @Modifying
    @Query(value = "UPDATE client SET type = 'CHEF' WHERE id = :clientId", nativeQuery = true)
    void changeClientType(@Param("clientId") Long clientId);
}

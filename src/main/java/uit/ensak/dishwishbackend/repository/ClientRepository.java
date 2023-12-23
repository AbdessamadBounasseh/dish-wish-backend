package uit.ensak.dishwishbackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uit.ensak.dishwishbackend.model.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findClientByEmail(String email);
    @Transactional
    @Modifying
    @Query(value = "UPDATE client SET type = 'CHEF' WHERE id = :clientId", nativeQuery = true)
    void changeClientType(@Param("clientId") Long clientId);
}

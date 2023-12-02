package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.ensak.dishwishbackend.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findClientByEmail(String email);
}

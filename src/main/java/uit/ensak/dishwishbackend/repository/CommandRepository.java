package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.ensak.dishwishbackend.model.Command;

import java.util.List;

public interface CommandRepository extends JpaRepository<Command, Long> {
    List<Command> findByClientIdAndStatus(Long id, String status);
    List<Command> findByChefIdAndStatus(Long id, String status);
}

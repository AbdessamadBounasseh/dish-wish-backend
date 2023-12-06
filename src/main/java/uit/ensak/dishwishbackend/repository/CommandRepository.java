package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.ensak.dishwishbackend.model.Command;

public interface CommandRepository extends JpaRepository<Command, Long> {
}

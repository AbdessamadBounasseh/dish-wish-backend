package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.ensak.dishwishbackend.model.VerificationToken;

public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
    void deleteByToken(String token);
}

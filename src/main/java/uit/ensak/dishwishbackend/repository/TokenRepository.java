package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.ensak.dishwishbackend.model.VerificationToken;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
    void deleteByToken(String token);

    Optional<VerificationToken> findByCode(String code);

    Optional<VerificationToken> findByToken(String token);
}

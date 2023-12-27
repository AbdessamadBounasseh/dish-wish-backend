package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByCode(String code);

    void deleteAllByClientId(Long id);

    Optional<PasswordResetToken> findByClientId(Long id);
}

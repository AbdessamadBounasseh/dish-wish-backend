package uit.ensak.dishwishbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uit.ensak.dishwishbackend.model.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<VerificationToken, Long> {

    void deleteByToken(String token);

    @Query(value = """
      select t from VerificationToken t inner join Client u\s
      on t.client.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<VerificationToken> findAllValidTokenByUserId(Long id);

    Optional<VerificationToken> findByCode(String code);

    Optional<VerificationToken> findByToken(String token);

    void deleteAllByClientId(Long id);

    List<VerificationToken> findAllByClientId(Long id);
}

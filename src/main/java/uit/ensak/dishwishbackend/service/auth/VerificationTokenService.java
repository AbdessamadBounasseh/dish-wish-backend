package uit.ensak.dishwishbackend.service.auth;

import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.model.VerificationToken;
import uit.ensak.dishwishbackend.repository.TokenRepository;

@Service
public class VerificationTokenService {

    private final TokenRepository tokenRepository;

    public VerificationTokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void deleteToken(String token){
        tokenRepository.deleteByToken(token);
    }
}

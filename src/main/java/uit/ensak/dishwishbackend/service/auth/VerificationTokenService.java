package uit.ensak.dishwishbackend.service.auth;

import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.exception.VerificationTokenNotFoundException;
import uit.ensak.dishwishbackend.model.VerificationToken;
import uit.ensak.dishwishbackend.repository.TokenRepository;

@Service
public class VerificationTokenService {

    private final TokenRepository tokenRepository;

    public VerificationTokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public VerificationToken getByToken(String token) throws VerificationTokenNotFoundException {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new VerificationTokenNotFoundException("Verification token by token '"+token+"' could not be found"));
    }

    public String getTokenByCode(String code) throws VerificationTokenNotFoundException {
        return tokenRepository.findByCode(code)
                .map(VerificationToken::getToken)
                .orElseThrow(() -> new VerificationTokenNotFoundException("Verification token by code '"+code+"' could not be found"));
    }

    public String getCodeByToken(String token) throws VerificationTokenNotFoundException {
        return tokenRepository.findByToken(token)
                .map(VerificationToken::getCode)
                .orElseThrow(() -> new VerificationTokenNotFoundException("Verification token by token '"+token+"' could not be found"));
    }

    public void deleteToken(String token){
        tokenRepository.deleteByToken(token);
    }
}

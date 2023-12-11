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

    public String getTokenByCode(String code) throws VerificationTokenNotFoundException {
        VerificationToken token = tokenRepository.findByCode(code)
                .orElseThrow(() -> new VerificationTokenNotFoundException("Verification token by code '"+code+"' could not be found"));
        return token.getToken();
    }

    public String getCodeByToken(String token) throws VerificationTokenNotFoundException {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new VerificationTokenNotFoundException("Verification token by token '"+token+"' could not be found"));;
        return verificationToken.getCode();
    }

    public void deleteToken(String token){
        tokenRepository.deleteByToken(token);
    }
}

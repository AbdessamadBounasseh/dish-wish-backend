package uit.ensak.dishwishbackend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.controller.auth.payloads.ChangePasswordRequest;
import uit.ensak.dishwishbackend.controller.auth.payloads.ResetPasswordRequest;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.InvalidResetTokenException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.PasswordResetToken;
import uit.ensak.dishwishbackend.model.VerificationToken;
import uit.ensak.dishwishbackend.repository.PasswordResetTokenRepository;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordService {

    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final PasswordResetTokenRepository passwordTokenRepository;

    @Value("${app.name}")
    private String appName;

    @Value("${string.password-reset}")
    private String passwordReset;

    private static final long TOKEN_EXPIRATION = 3600000; // one hour

    public void changePassword(ChangePasswordRequest passwordRequest, Principal connectedClient) {
        var client = (Client) ((UsernamePasswordAuthenticationToken) connectedClient)
                .getPrincipal();

        if (!passwordEncoder.matches(passwordRequest.getCurrentPassword(), client.getPassword())) {
            log.error("Wrong password");
            throw new IllegalStateException("Wrong password");
        }

        if (!passwordRequest.getNewPassword().equals(passwordRequest.getConfirmationPassword())) {
            log.error("Passwords are not the same");
            throw new IllegalStateException("Passwords are not the same");
        }

        log.info("Updating the password");

        client.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));

        clientService.saveClient(client);

        log.info("Password updated");
    }

    public void forgotPassword(String email) throws MessagingException, UnsupportedEncodingException, ClientNotFoundException {
        Client client = clientService.getClientByEmail(email);
        if (client == null) {
            throw new ClientNotFoundException("Client with email '" + email + "' could not be found");
        }

        String token = UUID.randomUUID().toString();
        String code = saveUserPasswordResetToken(client, token);

        sendForgotPasswordEmail(client, code);
    }

    public void sendForgotPasswordEmail(Client client, String code) throws MessagingException, UnsupportedEncodingException {
        String subject = passwordReset + " - " + appName;
        String senderName = appName;
        String content = "<p> Bonjour, </p>\n" +
                "<p>Bienvenue sur DishWish ! Nous sommes ravis de vous accueillir. </p>\n" +
                "<p>Veuillez utiliser le code suivant pour réinitialiser votre mot de passe : </p>\n" +
                "<p><b>Code de vérification : " + code + "</b></p>\n" +
                "<p> Nous vous remercions pour votre confiance et restons à votre disposition pour toute assistance. </p>" +
                "<p> À bientôt, <br> DishWish</p>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("mpsisup@gmail.com", senderName);
        messageHelper.setTo(client.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        mailSender.send(message);
    }

    @Transactional
    public String saveUserPasswordResetToken(Client client, String token) {

        PasswordResetToken optionalToken = passwordTokenRepository
                .findByClientId(client.getId()).orElse(null);

        if (optionalToken == null){
            UUID uuid = UUID.randomUUID();
            String code = uuid.toString().replaceAll("-", "").substring(0, 6);
            var passwordResetToken = new PasswordResetToken(token, code, client);
            passwordTokenRepository.save(passwordResetToken);
            return code;
        }
        else {
            UUID uuid = UUID.randomUUID();
            String code = uuid.toString().replaceAll("-", "").substring(0, 6);
            optionalToken.setToken(token);
            optionalToken.setCode(code);
            optionalToken.setClient(client);
            optionalToken.setExpirationTime(
                    new Date(System.currentTimeMillis() + TOKEN_EXPIRATION)
            );
            passwordTokenRepository.save(optionalToken);
            return code;
        }
    }

    public void resetPassword(ResetPasswordRequest passwordRequest, String code) throws InvalidResetTokenException {
        PasswordResetToken token = passwordTokenRepository.findByCode(code);
        if (token == null) {
            log.debug("No token matches this code");
            throw new InvalidResetTokenException("No token matches this code");
        }
        if (isTokenExpired(token)) {
            log.debug("Token is expired");
            throw new InvalidResetTokenException("Token is expired");
        }
        if (!passwordRequest.getNewPassword().equals(passwordRequest.getConfirmationPassword())) {
            log.error("Passwords are not the same");
            throw new IllegalStateException("Passwords are not the same");
        }

        log.info("Updating the password");

        Client client = token.getClient();
        client.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        clientService.saveClient(client);

        log.info("Password updated");
    }

    private boolean isTokenExpired(PasswordResetToken token) {
        final Calendar calendar = Calendar.getInstance();
        return token.getExpirationTime().before(calendar.getTime());
    }
}

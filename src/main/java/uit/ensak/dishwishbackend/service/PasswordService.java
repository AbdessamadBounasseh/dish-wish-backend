package uit.ensak.dishwishbackend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
import uit.ensak.dishwishbackend.model.Client;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordService {

    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${app.name}")
    private String appName;

    @Value("${string.password-reset}")
    private String passwordReset;

    public void changePassword(ChangePasswordRequest passwordRequest, Principal connectedClient) {
        var client = (Client) ((UsernamePasswordAuthenticationToken) connectedClient)
                .getPrincipal();

        if (!passwordEncoder.matches(passwordRequest.getCurrentPassword(), client.getPassword())){
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

        log.info("Updating the password");
    }

    public void forgotPassword(String email) throws MessagingException, UnsupportedEncodingException {
        Client client = clientService.getClientByEmail(email);
        sendForgotPasswordEmail(client);
    }

    public void sendForgotPasswordEmail(Client client) throws MessagingException, UnsupportedEncodingException {
        String subject = passwordReset + " - " + appName;
        String senderName = appName;
        String content = "<p> Bonjour, </p>\n" +
                "<p>Bienvenue sur DishWish ! Nous sommes ravis de vous accueillir. </p>\n" +
                "<p>Veuillez utiliser le code suivant pour réinitialiser votre mot de passe : </p>\n" +
                "<p><b>Code de vérification : " + "code" + "</b></p>\n" +
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
}

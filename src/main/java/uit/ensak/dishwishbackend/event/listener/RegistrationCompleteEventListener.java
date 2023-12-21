package uit.ensak.dishwishbackend.event.listener;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import uit.ensak.dishwishbackend.event.RegistrationCompleteEvent;
import uit.ensak.dishwishbackend.exception.VerificationTokenNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.service.ClientService;
import uit.ensak.dishwishbackend.service.auth.VerificationTokenService;

import java.io.UnsupportedEncodingException;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final ClientService clientService;
    private final JavaMailSender mailSender;
    private final VerificationTokenService tokenService;

    private Client client;

    @Value("${app.name}")
    private String appName;

    @Value("${mail.address}")
    private String mailAddress;

    @Value("${string.email-verification}")
    private String emailVerification;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        client = event.getClient();
        String token = event.getVerificationToken();

        try {
            String code = tokenService.getCodeByToken(token);

//            String url = event.getApplicationUrl() + "auth/register/verify-email?code=" + code;
            sendVerificationEmail(code);

        } catch (VerificationTokenNotFoundException | MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void sendVerificationEmail(String code) throws MessagingException, UnsupportedEncodingException {
        String subject = emailVerification + " - " + appName;
        String senderName = appName;
        String content = "<p> Bonjour, </p>\n" +
                "<p>Bienvenue sur DishWish ! Nous sommes enchantés de vous accueillir. </p>\n" +
                "<p>Veuillez utiliser le code de vérification suivant pour finaliser votre inscription sur DishWish :</p>\n" +
                "<p><b>Code de vérification : " + code + "</b></p>\n" +
                "<p> Nous tenons à vous remercier pour votre confiance et nous sommes impatients de vous accompagner dans cette nouvelle aventure. " +
                "Si vous avez des questions ou besoin d'assistance, n'hésitez pas à nous contacter. </p>\n" +
                "<p> À très bientôt, <br> DishWish</p>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(mailAddress, senderName);
        messageHelper.setTo(client.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        mailSender.send(message);
    }
}

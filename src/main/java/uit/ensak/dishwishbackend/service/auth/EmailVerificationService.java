package uit.ensak.dishwishbackend.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.event.RegistrationCompleteEvent;
import uit.ensak.dishwishbackend.model.Client;

@Service
public class EmailVerificationService {

    private final ApplicationEventPublisher publisher;
    private final HttpServletRequest request;

    public EmailVerificationService(ApplicationEventPublisher publisher, HttpServletRequest request) {
        this.publisher = publisher;
        this.request = request;
    }

    public void sendVerificationEmail(Client client, String verificationToken) {
        String applicationUrl = this.applicationUrl(request);
        publisher.publishEvent(new RegistrationCompleteEvent(client, verificationToken, applicationUrl));
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"
                + request.getServerName() + ":"
                + request.getServerPort() + "/"
                + request.getContextPath();
    }
}

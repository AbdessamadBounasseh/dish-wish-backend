package uit.ensak.dishwishbackend.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import uit.ensak.dishwishbackend.event.RegistrationCompleteEvent;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.service.ClientService;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final ClientService clientService;

    public RegistrationCompleteEventListener(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        Client client = event.getClient();
        String token = event.getVerificationToken();
        clientService.saveUserVerificationToken(client, token);
        String url = event.getApplicationUrl()+"auth/register/verify-email?token="+token;
        log.info("Click the link to verify your registration : {}", url);
    }
}

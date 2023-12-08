package uit.ensak.dishwishbackend.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import uit.ensak.dishwishbackend.model.Client;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private Client client;

    private String applicationUrl;

    private final String verificationToken;

    public RegistrationCompleteEvent(Client client, String applicationUrl, String verificationToken) {
        super(client);

        this.client = client;
        this.applicationUrl = applicationUrl;
        this.verificationToken = verificationToken;
    }
}

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

    public RegistrationCompleteEvent(Client client,
                                     String verificationToken,
                                     String applicationUrl
                                     ) {
        super(client);
        this.applicationUrl = applicationUrl;
        this.client = client;
        this.verificationToken = verificationToken;
    }
}

package uit.ensak.dishwishbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.service.ClientService;

@Configuration
public class ApplicationConfig {

    private final ClientService clientService;

    public ApplicationConfig(ClientService clientService) {
        this.clientService = clientService;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            try {
                return clientService.getClientByEmail(username)
                        .orElseThrow(() -> new ClientNotFoundException("Client by email " + username + " could not be found."));
            } catch (ClientNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        };
    }
}

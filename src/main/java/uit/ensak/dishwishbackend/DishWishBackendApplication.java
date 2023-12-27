package uit.ensak.dishwishbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.service.ClientService;
import uit.ensak.dishwishbackend.service.RoleService;

@SpringBootApplication
public class DishWishBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DishWishBackendApplication.class, args);
    }

//    @Bean
    CommandLineRunner run(ClientService clientService, RoleService roleService) {
        return args -> {
            Client client1 = new Client();
            client1.setFirstName("Abdessamad");
            client1.setLastName("Bounasseh");
            client1.setEmail("abdessamad@gmail.com");
            clientService.saveClient(client1);

            Client client2 = new Client();
            client2.setFirstName("Faycal");
            client2.setLastName("El Ourrate");
            client2.setEmail("faycal@gmail.com");
            clientService.saveClient(client2);

            Client client3 = new Client();
            client3.setFirstName("Meryem");
            client3.setLastName("El Hassouni");
            client3.setEmail("meryem@gmail.com");
            clientService.saveClient(client3);

            roleService.addRoleToUser("abdessamad@gmail.com", "CLIENT");
            roleService.addRoleToUser("faycal@gmail.com", "CHEF");
            roleService.addRoleToUser("meryem@gmail.com", "CLIENT");
        };
    }

}

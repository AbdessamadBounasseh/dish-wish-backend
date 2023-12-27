package uit.ensak.dishwishbackend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uit.ensak.dishwishbackend.model.Client;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void ClientRepository_findClientByEmail_returnTheClient() {

        //arrange
        String email = "test@mail.com";
        Client client = new Client();
        client.setEmail(email);
        clientRepository.save(client);

        //act
        Client retrieveClient = clientRepository.findClientByEmail(email)
                .orElseThrow();

        //assert
        assertThat(retrieveClient).isNotNull();
        assertThat(retrieveClient.getEmail()).isEqualTo(email);
    }
}
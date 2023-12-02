package uit.ensak.dishwishbackend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void gettingClientByIdSucceed() throws ClientNotFoundException {

        // given
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        clientRepository.save(client);

        // when
        Client retrieveClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Not Found !"));

        // then
        assertThat(retrieveClient.getId()).isEqualTo(client.getId());
    }

    @Test
    void clientByIdNotFound() {

        // given
        Long clientId = 1L;

        // when & then
        assertThrows(ClientNotFoundException.class, () -> {
            clientRepository.findById(clientId)
                    .orElseThrow(() -> new ClientNotFoundException("Not Found !"));
        });
    }
}
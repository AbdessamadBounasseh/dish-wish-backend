package uit.ensak.dishwishbackend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;

import static org.assertj.core.api.Assertions.assertThat;

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
        Client expected = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Not Found !"));

        // then
        assertThat(expected.getId()).isEqualTo(client.getId());
    }
}
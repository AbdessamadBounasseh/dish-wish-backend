package uit.ensak.dishwishbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.repository.ClientRepository;
import uit.ensak.dishwishbackend.repository.RoleRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private RoleRepository roleRepository;

    private ClientService clientService;

    @BeforeEach
    void setUp() {
        clientService = new ClientService(clientRepository, roleRepository);
    }

    @Test
    void getClientByIdSucceed() throws ClientNotFoundException {
        // given
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // when
        Client retrievedClient = clientService.getClientById(clientId);

        // then
        assertNotNull(retrievedClient);
        assertEquals(clientId, retrievedClient.getId());
    }

    @Test
    void getClientByIdThrowsClientNotFoundException() {
        // given
        Long clientId = 1L;

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // when and then
        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(clientId));
    }

    @Test
    void saveClientSucceed() {
        // given
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);

//        when(clientRepository.save(client)).thenReturn(client);

        // when
        clientService.saveClient(client);

        // then
        verify(clientRepository).save(client);
    }

    @Test
    void saveClientFailedBecauseOfDuplicatedIds() {
        // given
        Long clientId = 1L;

        Client client1 = new Client();
        client1.setId(clientId);

        Client client2 = new Client();
        client2.setId(clientId);

        doThrow(DataIntegrityViolationException.class)
                .when(clientRepository).save(client2);

        // when & then
        assertThrows(DataIntegrityViolationException.class,
                () -> clientService.saveClient(client2));
    }
}
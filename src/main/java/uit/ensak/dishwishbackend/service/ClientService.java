package uit.ensak.dishwishbackend.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.repository.ClientRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ClientService implements IClientSevice {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClientById(Long id) throws ClientNotFoundException {
        log.info("Fetching user by id {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client by Id " + id + " could not be found."));
    }

    public Client saveClient(Client client) {
        log.info("Saving new client {}", client);
        return clientRepository.save(client);
    }

    @Override
    public Client getClientByEmail(String email) {
        log.info("Fetching client by email {}", email);
        return clientRepository.findClientByEmail(email).orElseThrow();
    }

    @Override
    public List<Client> getAllClients() {
        log.info("Fetching all clients");
        return clientRepository.findAll();
    }

}

package uit.ensak.dishwishbackend.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Role;
import uit.ensak.dishwishbackend.repository.ClientRepository;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class RoleService implements IRoleService {

    private final ClientRepository clientRepository;

    public RoleService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void addRoleToUser(String email, String roleName) throws ClientNotFoundException {
        log.info("Adding role {} to user by email {}", roleName, email);

        Optional<Client> optionalClient = clientRepository.findClientByEmail(email);
        if (optionalClient.isEmpty()){
            throw new ClientNotFoundException("Client by email " + email + " could not be found.");
        }
        Client client = optionalClient.get();

        Role role = Role.valueOf(roleName);

        client.setRole(role);
    }

}

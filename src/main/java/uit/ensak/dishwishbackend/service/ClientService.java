package uit.ensak.dishwishbackend.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.RoleNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Role;
import uit.ensak.dishwishbackend.repository.ClientRepository;
import uit.ensak.dishwishbackend.repository.RoleRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ClientService implements IClientSevice {

    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;

    public ClientService(ClientRepository clientRepository, RoleRepository roleRepository) {
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
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
    public Role saveRole(Role role) {
        log.info("Saving new role {}", role);
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) throws ClientNotFoundException, RoleNotFoundException {
        log.info("Adding role {} to user by email {}", roleName, email);
        Client client = clientRepository.findClientByEmail(email);
        if (client == null){
            throw new ClientNotFoundException("Client by email " + email + " could not be found.");
        }

        Role role = roleRepository.findRoleByName(roleName);
        if (role == null){
            throw new RoleNotFoundException("Role by name " + roleName + " could not be found.");
        }
        client.getRoles().add(role);
    }

    @Override
    public Client getClientByEmail(String email) {
        log.info("Fetching client by email {}", email);
        return clientRepository.findClientByEmail(email);
    }

    @Override
    public List<Client> getAllClients() {
        log.info("Fetching all clients");
        return clientRepository.findAll();
    }

}

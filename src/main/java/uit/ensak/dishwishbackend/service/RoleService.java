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

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class RoleService implements IRoleSevice {

    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;

    public RoleService(RoleRepository roleRepository, ClientRepository clientRepository) {
        this.roleRepository = roleRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {}", role);
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) throws ClientNotFoundException, RoleNotFoundException {
        log.info("Adding role {} to user by email {}", roleName, email);

        Optional<Client> optionalClient = clientRepository.findClientByEmail(email);
        if (optionalClient.isEmpty()){
            throw new ClientNotFoundException("Client by email " + email + " could not be found.");
        }
        Client client = optionalClient.get();

        Role role = roleRepository.findRoleByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role by name " + roleName + " could not be found."));

        client.getRoles().add(role);
    }

}
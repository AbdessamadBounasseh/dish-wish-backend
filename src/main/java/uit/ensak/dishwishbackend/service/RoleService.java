package uit.ensak.dishwishbackend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Role;
import uit.ensak.dishwishbackend.repository.ClientRepository;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class RoleService implements IRoleService {
    private final ClientRepository clientRepository;

    @Override
    public void addRoleToUser(String email, String roleName) throws ClientNotFoundException {
        log.info("Adding role {} to user by email {}", roleName, email);
        Client client = clientRepository.findClientByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("Client by email " + email + " could not be found."));
        Role role = Role.valueOf(roleName);
        client.setRole(role);
    }
}

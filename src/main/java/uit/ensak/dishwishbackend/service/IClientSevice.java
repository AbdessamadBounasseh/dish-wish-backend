package uit.ensak.dishwishbackend.service;

import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.RoleNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Role;

import java.util.List;

public interface IClientSevice {

    Client getClientById(Long id) throws ClientNotFoundException;

    Client saveClient(Client client);

    Role saveRole(Role role);

    void addRoleToUser(String email, String roleName) throws ClientNotFoundException, RoleNotFoundException;

    Client getClientByEmail(String email);

    List<Client> getAllClients();
}

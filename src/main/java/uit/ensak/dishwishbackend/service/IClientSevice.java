package uit.ensak.dishwishbackend.service;

import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;

import java.util.List;
import java.util.Optional;

public interface IClientSevice {

    Client getClientById(Long id) throws ClientNotFoundException;

    Client saveClient(Client client);

    Optional<Client> getClientByEmail(String email);

    List<Client> getAllClients();
}

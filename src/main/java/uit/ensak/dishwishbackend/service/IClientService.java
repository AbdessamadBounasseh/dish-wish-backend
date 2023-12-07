package uit.ensak.dishwishbackend.service;

import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;

import java.util.List;

public interface IClientService {

    Client getClientById(long id) throws ClientNotFoundException;

    Client saveClient(Client client);

    Client getClientByEmail(String email);

    List<Client> getAllClients();

    void saveUserVerificationToken(Client client, String token);
}

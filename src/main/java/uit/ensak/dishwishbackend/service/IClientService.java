package uit.ensak.dishwishbackend.service;

import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;

import java.util.List;

public interface IClientService {

    Client getClientById(Long id) throws ClientNotFoundException;

    Client getClientByEmail(String email);

    List<Client> getAllClients();

    void saveUserVerificationToken(Client client, String token);

    Client saveClient(Client client);

    void revokeAllUserTokens(Client client);
}

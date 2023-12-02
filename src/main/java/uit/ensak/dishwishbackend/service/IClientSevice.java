package uit.ensak.dishwishbackend.service;

import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;

public interface IClientSevice {

    Client getClientById(Long id) throws ClientNotFoundException;

    void saveClient(Client client);

}

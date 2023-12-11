package uit.ensak.dishwishbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.repository.ClientRepository;

@Service
public class ClientService implements IClientSevice {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClientById(Long id) throws ClientNotFoundException {
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client by Id "+ id +" could not be found."));
    }

    public void saveClient(Client client) {
        clientRepository.save(client);
    }


    @Transactional
    public void switchRole(Long clientId) throws ClientNotFoundException {
        Client client = getClientById(clientId);

        if (client instanceof Chef) {
            // If the client is a chef, switch to client
            Client updatedClient = new Client();
            copyFields(client, updatedClient);
            //updatedClient.setId(clientId);
            clientRepository.save(updatedClient);
        } else if (client instanceof Client) {
            // If the client is a regular client, switch to chef
            Chef updatedChef = new Chef();
            copyFields(client, updatedChef);
            //updatedChef.setId(clientId);
            clientRepository.save(updatedChef);
        }

        clientRepository.delete(client);
    }

    private void copyFields(Client source, Client target) {
        // Copy common fields from source to target
        target.setEmail(source.getEmail());
        target.setPassword(source.getPassword());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setAddress(source.getAddress());
        target.setPhoneNumber(source.getPhoneNumber());
        target.setPhoto(source.getPhoto());

        // Check if source is a Chef before copying Chef-specific fields
        if (source instanceof Chef && target instanceof Chef) {
            Chef sourceChef = (Chef) source;
            Chef targetChef = (Chef) target;
            targetChef.setBio(sourceChef.getBio());
            targetChef.setIdCard(sourceChef.getIdCard());
            targetChef.setCertificate(sourceChef.getCertificate());
        }
    }





}

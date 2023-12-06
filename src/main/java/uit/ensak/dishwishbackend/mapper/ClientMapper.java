package uit.ensak.dishwishbackend.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import uit.ensak.dishwishbackend.dto.ClientDTO;
import uit.ensak.dishwishbackend.model.Client;

@Component
public class ClientMapper {
    public Client fromClientDTO(ClientDTO clientDTO){
        Client client = new Client();
        BeanUtils.copyProperties(clientDTO, client);
        return client;
    }
    public ClientDTO fromClient(Client client){
        ClientDTO clientDTO = new ClientDTO();
        BeanUtils.copyProperties(client, clientDTO);
        return clientDTO;
    }
}

package uit.ensak.dishwishbackend.mapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import uit.ensak.dishwishbackend.dto.ClientDTO;
import uit.ensak.dishwishbackend.model.Client;

@Component
@Transactional
@AllArgsConstructor
public class ClientMapper {
    private final DietMapper dietMapper;
    public Client fromClientDtoToClient(ClientDTO clientDTO, Client client){
        BeanUtils.copyProperties(clientDTO, client);
        if (clientDTO.getDietDTO() != null) {
            client.setDiet( dietMapper.fromDietDtoToDiet(clientDTO.getDietDTO()));
        }
        return client;
    }
    public ClientDTO fromClientToClientDto(Client client){
        ClientDTO clientDTO = new ClientDTO();
        BeanUtils.copyProperties(client, clientDTO);
        if (client.getDiet() != null) {
            clientDTO.setDietDTO(dietMapper.fromDietToDietDto(client.getDiet()));
        }
        return clientDTO;
    }
}

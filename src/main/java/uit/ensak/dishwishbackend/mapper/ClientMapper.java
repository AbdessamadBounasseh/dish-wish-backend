package uit.ensak.dishwishbackend.mapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import uit.ensak.dishwishbackend.dto.ClientDTO;
import uit.ensak.dishwishbackend.model.Client;

import java.util.stream.Collectors;

@Component
@Transactional
@AllArgsConstructor
public class ClientMapper {
    private final DietMapper dietMapper;
    private final AllergyMapper allergyMapper;
    public Client fromClientDtoToClient(ClientDTO clientDTO, Client client){
        System.out.println("hello");
        BeanUtils.copyProperties(clientDTO, client);
        System.out.println("hello");
        System.out.println(client);
        if (clientDTO.getDietsDTO() != null) {
            client.setDiets(clientDTO.getDietsDTO().stream()
                    .map(dietDTO -> dietMapper.fromDietDtoToDiet(dietDTO, client))
                    .collect(Collectors.toList()));
        }
        if (clientDTO.getAllergiesDTO() != null) {
            client.setAllergies(clientDTO.getAllergiesDTO().stream()
                    .map(allergyDTO ->allergyMapper.fromAllergyDtoToAllergy(allergyDTO, client))
                    .collect(Collectors.toList()));
        }
        System.out.println("hello");
        return client;
    }
    public ClientDTO fromClientToClientDto(Client client){
        ClientDTO clientDTO = new ClientDTO();
        BeanUtils.copyProperties(client, clientDTO);
        if (client.getDiets() != null) {
            clientDTO.setDietsDTO(client.getDiets().stream()
                    .map(dietMapper::fromDietToDietDto)
                    .collect(Collectors.toList()));
        }
        if (client.getAllergies() != null) {
            clientDTO.setAllergiesDTO(client.getAllergies().stream()
                    .map(allergyMapper::fromAllergyToAllergyDto)
                    .collect(Collectors.toList()));
        }
        return clientDTO;
    }
}

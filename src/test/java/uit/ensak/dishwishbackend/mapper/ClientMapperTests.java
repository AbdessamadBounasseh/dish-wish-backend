package uit.ensak.dishwishbackend.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uit.ensak.dishwishbackend.dto.ClientDTO;
import uit.ensak.dishwishbackend.dto.DietDTO;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Diet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientMapperTests {
    @Mock
    private DietMapper dietMapper;
    @InjectMocks
    private ClientMapper clientMapper;

    @Test
    public void ClientMapper_fromClientDtoToClient_ReturnClient(){
        Long clientId = 1L;
        DietDTO dietDTO1 = mock(DietDTO.class);
        Diet diet1 = mock(Diet.class);

        ClientDTO clientDTO = ClientDTO.builder().firstName("nash").lastName("Omega").phoneNumber("07070707")
                .address("Kénitra").dietDTO(dietDTO1).build();
        Client client = Client.builder().id(clientId).build();

        when(dietMapper.fromDietDtoToDiet(any(DietDTO.class))).thenReturn(diet1);

        Client clientReturn = clientMapper.fromClientDtoToClient(clientDTO, client);

        Assertions.assertInstanceOf(Client.class, clientReturn);
        Assertions.assertEquals(clientDTO.getFirstName(), clientReturn.getFirstName());
        Assertions.assertEquals(clientDTO.getLastName(), clientReturn.getLastName());
        Assertions.assertEquals(clientDTO.getPhoneNumber(), clientReturn.getPhoneNumber());
        Assertions.assertEquals(clientDTO.getAddress(), clientReturn.getAddress());
        Assertions.assertNotNull(clientReturn.getDiet());
    }

    @Test
    public void ClientMapper_fromClientToClientDto_ReturnClientDto(){
        Long clientId = 1L;
        DietDTO dietDTO1 = mock(DietDTO.class);
        Diet diet1 = mock(Diet.class);

        Client client = Client.builder().id(clientId).firstName("nash").lastName("Omega").phoneNumber("07070707")
                .address("Kénitra").diet(diet1).build();

        when(dietMapper.fromDietToDietDto(any(Diet.class))).thenReturn(dietDTO1);

        ClientDTO clientDTOReturn = clientMapper.fromClientToClientDto(client);

        Assertions.assertInstanceOf(ClientDTO.class, clientDTOReturn);
        Assertions.assertEquals(client.getFirstName(), clientDTOReturn.getFirstName());
        Assertions.assertEquals(client.getLastName(), clientDTOReturn.getLastName());
        Assertions.assertEquals(client.getPhoneNumber(), clientDTOReturn.getPhoneNumber());
        Assertions.assertEquals(client.getAddress(), clientDTOReturn.getAddress());
        Assertions.assertNotNull(clientDTOReturn.getDietDTO());
    }
}

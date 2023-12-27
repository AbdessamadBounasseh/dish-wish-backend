package uit.ensak.dishwishbackend.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uit.ensak.dishwishbackend.dto.AllergyDTO;
import uit.ensak.dishwishbackend.dto.ClientDTO;
import uit.ensak.dishwishbackend.dto.DietDTO;
import uit.ensak.dishwishbackend.model.Allergy;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Diet;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientMapperTests {
    @Mock
    private DietMapper dietMapper;
    @Mock
    private AllergyMapper allergyMapper;
    @InjectMocks
    private ClientMapper clientMapper;

    @Test
    public void ClientMapper_fromClientDtoToClient_ReturnClient(){
        Long clientId = 1L;
        DietDTO dietDTO1 = mock(DietDTO.class);
        DietDTO dietDTO2 = mock(DietDTO.class);
        Diet diet1 = mock(Diet.class);
        Diet diet2 = mock(Diet.class);
        AllergyDTO allergyDTO1 = mock(AllergyDTO.class);
        AllergyDTO allergyDTO2 = mock(AllergyDTO.class);
        Allergy allergy1 = mock(Allergy.class);
        Allergy allergy2 = mock(Allergy.class);
        ClientDTO clientDTO = ClientDTO.builder().firstName("nash").lastName("Omega").phoneNumber("07070707")
                .address("Kénitra").dietsDTO(Arrays.asList(dietDTO1, dietDTO2)).allergiesDTO(Arrays.asList(allergyDTO1, allergyDTO2)).build();
        Client client = Client.builder().id(clientId).build();

        when(dietMapper.fromDietDtoToDiet(any(DietDTO.class),eq(client))).thenReturn(diet1).thenReturn(diet2);
        when(allergyMapper.fromAllergyDtoToAllergy(any(AllergyDTO.class),eq(client))).thenReturn(allergy1).thenReturn(allergy2);

        Client clientReturn = clientMapper.fromClientDtoToClient(clientDTO, client);

        Assertions.assertInstanceOf(Client.class, clientReturn);
        Assertions.assertEquals(clientDTO.getFirstName(), clientReturn.getFirstName());
        Assertions.assertEquals(clientDTO.getLastName(), clientReturn.getLastName());
        Assertions.assertEquals(clientDTO.getPhoneNumber(), clientReturn.getPhoneNumber());
        Assertions.assertEquals(clientDTO.getAddress(), clientReturn.getAddress());
        Assertions.assertEquals(2, clientReturn.getAllergies().size());
        Assertions.assertEquals(2, clientReturn.getDiets().size());
    }

    @Test
    public void ClientMapper_fromClientToClientDto_ReturnClientDto(){
        Long clientId = 1L;
        DietDTO dietDTO1 = mock(DietDTO.class);
        DietDTO dietDTO2 = mock(DietDTO.class);
        Diet diet1 = mock(Diet.class);
        Diet diet2 = mock(Diet.class);
        AllergyDTO allergyDTO1 = mock(AllergyDTO.class);
        AllergyDTO allergyDTO2 = mock(AllergyDTO.class);
        Allergy allergy1 = mock(Allergy.class);
        Allergy allergy2 = mock(Allergy.class);
        Client client = Client.builder().id(clientId).firstName("nash").lastName("Omega").phoneNumber("07070707")
                .address("Kénitra").diets(Arrays.asList(diet1, diet2)).allergies(Arrays.asList(allergy1, allergy2)).build();

        when(dietMapper.fromDietToDietDto(any(Diet.class))).thenReturn(dietDTO1).thenReturn(dietDTO2);
        when(allergyMapper.fromAllergyToAllergyDto(any(Allergy.class))).thenReturn(allergyDTO1).thenReturn(allergyDTO2);

        ClientDTO clientDTOReturn = clientMapper.fromClientToClientDto(client);

        Assertions.assertInstanceOf(ClientDTO.class, clientDTOReturn);
        Assertions.assertEquals(client.getFirstName(), clientDTOReturn.getFirstName());
        Assertions.assertEquals(client.getLastName(), clientDTOReturn.getLastName());
        Assertions.assertEquals(client.getPhoneNumber(), clientDTOReturn.getPhoneNumber());
        Assertions.assertEquals(client.getAddress(), clientDTOReturn.getAddress());
        Assertions.assertEquals(2, clientDTOReturn.getAllergiesDTO().size());
        Assertions.assertEquals(2, clientDTOReturn.getDietsDTO().size());
    }
}

package uit.ensak.dishwishbackend.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uit.ensak.dishwishbackend.dto.AllergyDTO;
import uit.ensak.dishwishbackend.model.Allergy;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.repository.AllergyRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AllergyMapperTests {
    @Mock
    private AllergyRepository allergyRepository;
    @InjectMocks
    private AllergyMapper allergyMapper;

    @Test
    public void AllergyMapper_fromAllergyDtoToAllergy_ReturnAllergy() {
        Client client1 = Client.builder().id(1L).email("nash1@gmail.com").build();
        Allergy allergy = Allergy.builder().id(1L).title("Oeuf").clients(new ArrayList<>()).build();
        AllergyDTO allergyDTO = AllergyDTO.builder().title("Oeuf").build();

        when(allergyRepository.findByTitle(anyString())).thenReturn(Optional.of(allergy));

        Allergy allergyReturn = allergyMapper.fromAllergyDtoToAllergy(allergyDTO, client1);
        allergy.getClients().add(client1);

        Assertions.assertEquals(allergy, allergyReturn);
    }

    @Test
    public void AllergyMapper_fromAllergyToAllergyDto_ReturnAllergyDto(){
        Client client1 = Client.builder().id(1L).email("nash1@gmail.com").build();
        Allergy allergy = Allergy.builder().id(1L).title("Oeuf").clients(Collections.singletonList(client1)).build();

        AllergyDTO allergyDTOReturn = allergyMapper.fromAllergyToAllergyDto(allergy);

        Assertions.assertInstanceOf(AllergyDTO.class, allergyDTOReturn);
        Assertions.assertEquals(allergy.getTitle(),allergyDTOReturn.getTitle());
    }
}


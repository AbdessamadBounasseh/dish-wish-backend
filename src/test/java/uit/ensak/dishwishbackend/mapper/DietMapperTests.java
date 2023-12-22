package uit.ensak.dishwishbackend.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uit.ensak.dishwishbackend.dto.DietDTO;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Diet;
import uit.ensak.dishwishbackend.repository.DietRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DietMapperTests {
    @Mock
    private DietRepository dietRepository;
    @InjectMocks
    private DietMapper dietMapper;

    @Test
    public void DietMapper_fromDietDtoToDiet_ReturnDiet() {
        Long dietId = 1L;
        Client client1 = Client.builder().id(1L).email("nash1@gmail.com").build();
        Diet diet = Diet.builder().id(1L).title("Végétarien").clients(new ArrayList<>()).build();
        DietDTO dietDTO = DietDTO.builder().id(dietId).title("Végétarien").build();

        when(dietRepository.findById(any(Long.class))).thenReturn(Optional.of(diet));

        Diet dietReturn = dietMapper.fromDietDtoToDiet(dietDTO, client1);
        diet.getClients().add(client1);

        Assertions.assertEquals(diet, dietReturn);
    }

    @Test
    public void DietMapper_fromDietToDietDto_ReturnDietDto(){
        Client client1 = Client.builder().id(1L).email("nash1@gmail.com").build();
        Diet diet = Diet.builder().id(1L).title("Végétarien").clients(Collections.singletonList(client1)).build();

        DietDTO dietDTOReturn = dietMapper.fromDietToDietDto(diet);

        Assertions.assertInstanceOf(DietDTO.class, dietDTOReturn);
        Assertions.assertEquals(diet.getTitle(),dietDTOReturn.getTitle());
        Assertions.assertEquals(diet.getId(),dietDTOReturn.getId());
    }
}

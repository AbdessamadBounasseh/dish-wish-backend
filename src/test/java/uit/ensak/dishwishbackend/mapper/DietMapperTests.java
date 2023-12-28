package uit.ensak.dishwishbackend.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uit.ensak.dishwishbackend.dto.DietDTO;
import uit.ensak.dishwishbackend.model.Diet;
import uit.ensak.dishwishbackend.repository.DietRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DietMapperTests {
    @Mock
    private DietRepository dietRepository;
    @InjectMocks
    private DietMapper dietMapper;

    @Test
    public void DietMapper_fromDietDtoToDiet_ReturnDiet() {
        Diet diet = Diet.builder().id(1L).title("Végétarien").build();
        DietDTO dietDTO = DietDTO.builder().title("Végétarien").build();

        when(dietRepository.findByTitle(anyString())).thenReturn(Optional.of(diet));

        Diet dietReturn = dietMapper.fromDietDtoToDiet(dietDTO);

        Assertions.assertEquals(diet, dietReturn);
    }

    @Test
    public void DietMapper_fromDietToDietDto_ReturnDietDto(){
        Diet diet = Diet.builder().id(1L).title("Végétarien").build();

        DietDTO dietDTOReturn = dietMapper.fromDietToDietDto(diet);

        Assertions.assertInstanceOf(DietDTO.class, dietDTOReturn);
        Assertions.assertEquals(diet.getTitle(),dietDTOReturn.getTitle());
    }
}

package uit.ensak.dishwishbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uit.ensak.dishwishbackend.dto.DietDTO;
import uit.ensak.dishwishbackend.mapper.DietMapper;
import uit.ensak.dishwishbackend.model.Diet;
import uit.ensak.dishwishbackend.repository.DietRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DietServiceTest {
    @Mock
    private DietRepository dietRepository;
    @Mock
    private DietMapper dietMapper;
    @InjectMocks
    private DietService dietService;

    @Test
    public void DietService_getAllDiets_ReturnDietDTOList(){
        Diet diet1 = mock(Diet.class);
        Diet diet2 = mock(Diet.class);
        DietDTO dietDTO1 = mock(DietDTO.class);
        DietDTO dietDTO2 = mock(DietDTO.class);

        when(dietRepository.findAll()).thenReturn(Stream.of(diet1, diet2)
                .collect(Collectors.toList()));
        when(dietMapper.fromDietToDietDto(any(Diet.class))).thenReturn(dietDTO1).thenReturn(dietDTO2);

        List<DietDTO> returnDTOS = dietService.getAllDiets();

        Assertions.assertEquals(2, returnDTOS.size());
    }
}

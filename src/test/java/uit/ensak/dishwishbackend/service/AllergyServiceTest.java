package uit.ensak.dishwishbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uit.ensak.dishwishbackend.dto.AllergyDTO;
import uit.ensak.dishwishbackend.mapper.AllergyMapper;
import uit.ensak.dishwishbackend.model.Allergy;
import uit.ensak.dishwishbackend.repository.AllergyRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AllergyServiceTest {
    @Mock
    private AllergyRepository allergyRepository;
    @Mock
    private AllergyMapper allergyMapper;
    @InjectMocks
    private AllergyService allergyService;

    @Test
    public void AllergyService_getAllAllergies_ReturnAllergyDTOList(){
        Allergy allergy1 = mock(Allergy.class);
        Allergy allergy2 = mock(Allergy.class);
        AllergyDTO allergyDTO1 = mock(AllergyDTO.class);
        AllergyDTO allergyDTO2 = mock(AllergyDTO.class);

        when(allergyRepository.findAll()).thenReturn(Stream.of(allergy1, allergy2)
                .collect(Collectors.toList()));
        when(allergyMapper.fromAllergyToAllergyDto(any(Allergy.class))).thenReturn(allergyDTO1).thenReturn(allergyDTO2);

        List<AllergyDTO> returnDTOS = allergyService.getAllAllergies();

        Assertions.assertEquals(2, returnDTOS.size());
    }
}

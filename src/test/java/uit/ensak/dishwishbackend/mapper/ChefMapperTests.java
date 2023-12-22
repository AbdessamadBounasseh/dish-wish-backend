package uit.ensak.dishwishbackend.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uit.ensak.dishwishbackend.dto.AllergyDTO;
import uit.ensak.dishwishbackend.dto.ChefDTO;
import uit.ensak.dishwishbackend.dto.DietDTO;
import uit.ensak.dishwishbackend.model.Allergy;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Diet;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChefMapperTests {
    @Mock
    private DietMapper dietMapper;
    @Mock
    private AllergyMapper allergyMapper;
    @InjectMocks
    private ChefMapper chefMapper;

    @Test
    public void ChefMapper_fromChefDtoToChef_ReturnChef(){
        Long chefId = 1L;
        DietDTO dietDTO1 = mock(DietDTO.class);
        DietDTO dietDTO2 = mock(DietDTO.class);
        Diet diet1 = mock(Diet.class);
        Diet diet2 = mock(Diet.class);
        AllergyDTO allergyDTO1 = mock(AllergyDTO.class);
        AllergyDTO allergyDTO2 = mock(AllergyDTO.class);
        Allergy allergy1 = mock(Allergy.class);
        Allergy allergy2 = mock(Allergy.class);

        ChefDTO chefDTO = new ChefDTO();
        chefDTO.setFirstName("nash");
        chefDTO.setLastName("Omega");
        chefDTO.setPhoneNumber("07070707");
        chefDTO.setAddress("Kénitra");
        chefDTO.setDietsDTO(Arrays.asList(dietDTO1, dietDTO2));
        chefDTO.setAllergiesDTO(Arrays.asList(allergyDTO1, allergyDTO2));
        chefDTO.setBio("Je suis un cuisinier");

        Chef chef = new Chef();
        chef.setId(chefId);

        when(dietMapper.fromDietDtoToDiet(any(DietDTO.class),eq(chef))).thenReturn(diet1).thenReturn(diet2);
        when(allergyMapper.fromAllergyDtoToAllergy(any(AllergyDTO.class),eq(chef))).thenReturn(allergy1).thenReturn(allergy2);

        Chef chefReturn = chefMapper.fromChefDtoToChef(chefDTO, chef);

        Assertions.assertInstanceOf(Chef.class, chefReturn);
        Assertions.assertEquals(chefDTO.getFirstName(), chefReturn.getFirstName());
        Assertions.assertEquals(chefDTO.getLastName(), chefReturn.getLastName());
        Assertions.assertEquals(chefDTO.getPhoneNumber(), chefReturn.getPhoneNumber());
        Assertions.assertEquals(chefDTO.getAddress(), chefReturn.getAddress());
        Assertions.assertEquals(chefDTO.getBio(), chefReturn.getBio());
        Assertions.assertEquals(2, chefReturn.getAllergies().size());
        Assertions.assertEquals(2, chefReturn.getDiets().size());
    }

    @Test
    public void ChefMapper_fromChefToChefDto_ReturnChefDto(){
        Long chefId = 1L;
        DietDTO dietDTO1 = mock(DietDTO.class);
        DietDTO dietDTO2 = mock(DietDTO.class);
        Diet diet1 = mock(Diet.class);
        Diet diet2 = mock(Diet.class);
        AllergyDTO allergyDTO1 = mock(AllergyDTO.class);
        AllergyDTO allergyDTO2 = mock(AllergyDTO.class);
        Allergy allergy1 = mock(Allergy.class);
        Allergy allergy2 = mock(Allergy.class);
        Chef chef = new Chef();
        chef.setId(chefId);
        chef.setFirstName("nash");
        chef.setLastName("Omega");
        chef.setPhoneNumber("07070707");
        chef.setAddress("Kénitra");
        chef.setDiets(Arrays.asList(diet1, diet2));
        chef.setAllergies(Arrays.asList(allergy1, allergy2));
        chef.setBio("Je suis un cuisinier");

        when(dietMapper.fromDietToDietDto(any(Diet.class))).thenReturn(dietDTO1).thenReturn(dietDTO2);
        when(allergyMapper.fromAllergyToAllergyDto(any(Allergy.class))).thenReturn(allergyDTO1).thenReturn(allergyDTO2);

        ChefDTO chefDTOReturn = chefMapper.fromChefToChefDto(chef);

        Assertions.assertInstanceOf(ChefDTO.class, chefDTOReturn);
        Assertions.assertEquals(chef.getFirstName(), chefDTOReturn.getFirstName());
        Assertions.assertEquals(chef.getLastName(), chefDTOReturn.getLastName());
        Assertions.assertEquals(chef.getPhoneNumber(), chefDTOReturn.getPhoneNumber());
        Assertions.assertEquals(chef.getAddress(), chefDTOReturn.getAddress());
        Assertions.assertEquals(2, chefDTOReturn.getAllergiesDTO().size());
        Assertions.assertEquals(2, chefDTOReturn.getDietsDTO().size());
    }
}

package uit.ensak.dishwishbackend.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uit.ensak.dishwishbackend.dto.ChefDTO;
import uit.ensak.dishwishbackend.dto.DietDTO;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Diet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChefMapperTests {
    @Mock
    private DietMapper dietMapper;
    @InjectMocks
    private ChefMapper chefMapper;

    @Test
    public void ChefMapper_fromChefDtoToChef_ReturnChef(){
        Long chefId = 1L;
        DietDTO dietDTO1 = mock(DietDTO.class);
        Diet diet1 = mock(Diet.class);

        ChefDTO chefDTO = new ChefDTO();
        chefDTO.setFirstName("nash");
        chefDTO.setLastName("Omega");
        chefDTO.setPhoneNumber("07070707");
        chefDTO.setAddress("Kénitra");
        chefDTO.setDietDTO(dietDTO1);
        chefDTO.setBio("Je suis un cuisinier");

        Chef chef = new Chef();
        chef.setId(chefId);

        when(dietMapper.fromDietDtoToDiet(any(DietDTO.class))).thenReturn(diet1);

        Chef chefReturn = chefMapper.fromChefDtoToChef(chefDTO, chef);

        Assertions.assertInstanceOf(Chef.class, chefReturn);
        Assertions.assertEquals(chefDTO.getFirstName(), chefReturn.getFirstName());
        Assertions.assertEquals(chefDTO.getLastName(), chefReturn.getLastName());
        Assertions.assertEquals(chefDTO.getPhoneNumber(), chefReturn.getPhoneNumber());
        Assertions.assertEquals(chefDTO.getAddress(), chefReturn.getAddress());
        Assertions.assertEquals(chefDTO.getBio(), chefReturn.getBio());
        Assertions.assertNotNull(chefReturn.getDiet());
    }

    @Test
    public void ChefMapper_fromChefToChefDto_ReturnChefDto(){
        Long chefId = 1L;
        DietDTO dietDTO1 = mock(DietDTO.class);
        Diet diet1 = mock(Diet.class);
        Chef chef = new Chef();
        chef.setId(chefId);
        chef.setFirstName("nash");
        chef.setLastName("Omega");
        chef.setPhoneNumber("07070707");
        chef.setAddress("Kénitra");
        chef.setDiet(diet1);
        chef.setBio("Je suis un cuisinier");

        when(dietMapper.fromDietToDietDto(any(Diet.class))).thenReturn(dietDTO1);

        ChefDTO chefDTOReturn = chefMapper.fromChefToChefDto(chef);

        Assertions.assertInstanceOf(ChefDTO.class, chefDTOReturn);
        Assertions.assertEquals(chef.getFirstName(), chefDTOReturn.getFirstName());
        Assertions.assertEquals(chef.getLastName(), chefDTOReturn.getLastName());
        Assertions.assertEquals(chef.getPhoneNumber(), chefDTOReturn.getPhoneNumber());
        Assertions.assertEquals(chef.getAddress(), chefDTOReturn.getAddress());
        Assertions.assertNotNull(chefDTOReturn.getDietDTO());
    }
}

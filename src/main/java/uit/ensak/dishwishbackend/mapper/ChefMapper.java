package uit.ensak.dishwishbackend.mapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import uit.ensak.dishwishbackend.dto.ChefDTO;
import uit.ensak.dishwishbackend.model.Chef;

@Component
@Transactional
@AllArgsConstructor
public class ChefMapper {
    private final DietMapper dietMapper;

    public Chef fromChefDtoToChef(ChefDTO chefDTO, Chef chef){
        BeanUtils.copyProperties(chefDTO, chef);
        if (chefDTO.getDietDTO() != null) {
            chef.setDiet( dietMapper.fromDietDtoToDiet(chefDTO.getDietDTO()));
        }
        return chef;
    }
    public ChefDTO fromChefToChefDto(Chef chef){
        ChefDTO chefDTO = new ChefDTO();
        BeanUtils.copyProperties(chef, chefDTO);
        if (chef.getDiet() != null) {
            chefDTO.setDietDTO(dietMapper.fromDietToDietDto(chef.getDiet()));
        }
        return chefDTO;
    }
}

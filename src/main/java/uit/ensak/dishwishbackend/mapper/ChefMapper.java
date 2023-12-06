package uit.ensak.dishwishbackend.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import uit.ensak.dishwishbackend.dto.ChefDTO;
import uit.ensak.dishwishbackend.model.Chef;

@Component
public class ChefMapper {
    public Chef fromChefDTO(ChefDTO chefDTO){
        Chef chef = new Chef();
        BeanUtils.copyProperties(chefDTO, chef);
        return chef;
    }
    public ChefDTO fromChef(Chef chef){
        ChefDTO chefDTO = new ChefDTO();
        BeanUtils.copyProperties(chef, chefDTO);
        return chefDTO;
    }
}

package uit.ensak.dishwishbackend.mapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import uit.ensak.dishwishbackend.dto.ChefDTO;
import uit.ensak.dishwishbackend.model.Chef;

import java.util.stream.Collectors;

@Component
@Transactional
@AllArgsConstructor
public class ChefMapper {
    private final DietMapper dietMapper;
    private final AllergyMapper allergyMapper;
    public Chef fromChefDtoToChef(ChefDTO chefDTO, Chef chef){
        BeanUtils.copyProperties(chefDTO, chef);
        if (chefDTO.getDietsDTO() != null) {
            chef.setDiets(chefDTO.getDietsDTO().stream()
                    .map(dietDTO -> dietMapper.fromDietDtoToDiet(dietDTO, chef))
                    .collect(Collectors.toList()));
        }
        if (chefDTO.getAllergiesDTO() != null) {
            chef.setAllergies(chefDTO.getAllergiesDTO().stream()
                    .map(allergyDTO ->allergyMapper.fromAllergyDtoToAllergy(allergyDTO, chef))
                    .collect(Collectors.toList()));
        }
        return chef;
    }
    public ChefDTO fromChefToChefDto(Chef chef){
        ChefDTO chefDTO = new ChefDTO();
        BeanUtils.copyProperties(chef, chefDTO);
        if (chef.getDiets() != null) {
            chefDTO.setDietsDTO(chef.getDiets().stream()
                    .map(dietMapper::fromDietToDietDto)
                    .collect(Collectors.toList()));
        }
        if (chef.getAllergies() != null) {
            chefDTO.setAllergiesDTO(chef.getAllergies().stream()
                    .map(allergyMapper::fromAllergyToAllergyDto)
                    .collect(Collectors.toList()));
        }
        return chefDTO;
    }
}

package uit.ensak.dishwishbackend.mapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import uit.ensak.dishwishbackend.dto.DietDTO;
import uit.ensak.dishwishbackend.model.Diet;
import uit.ensak.dishwishbackend.repository.DietRepository;

@Component
@Transactional
@AllArgsConstructor
public class DietMapper {
    private final DietRepository dietRepository;

    public Diet fromDietDtoToDiet(DietDTO dietDTO) {
        return dietRepository.findByTitle(dietDTO.getTitle()).orElseThrow();
    }

    public DietDTO fromDietToDietDto(Diet diet) {
        return new DietDTO(diet.getTitle());
    }
}

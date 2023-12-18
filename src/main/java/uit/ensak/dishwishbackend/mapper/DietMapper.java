package uit.ensak.dishwishbackend.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import uit.ensak.dishwishbackend.dto.DietDTO;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Diet;
import uit.ensak.dishwishbackend.repository.DietRepository;

@Component
@AllArgsConstructor
public class DietMapper {
    private final DietRepository dietRepository;

    public Diet fromDietDtoToDiet(DietDTO dietDTO, Client client) {
        Diet diet = dietRepository.findById(dietDTO.getId()).get();
        diet.getClients().add(client);
        return dietRepository.save(diet);

    }

    public DietDTO fromDietToDietDto(Diet diet) {
        return new DietDTO(diet.getId(), diet.getTitle());
    }
}

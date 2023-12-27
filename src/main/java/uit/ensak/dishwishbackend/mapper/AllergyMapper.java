package uit.ensak.dishwishbackend.mapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import uit.ensak.dishwishbackend.dto.AllergyDTO;
import uit.ensak.dishwishbackend.model.Allergy;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.repository.AllergyRepository;

@Component
@Transactional
@AllArgsConstructor
public class AllergyMapper {

    private final AllergyRepository allergyRepository;

    public Allergy fromAllergyDtoToAllergy(AllergyDTO allergyDTO, Client client) {
        Allergy allergy = allergyRepository.findByTitle(allergyDTO.getTitle()).orElseThrow();
        allergy.getClients().add(client);
        return allergy;
    }
    public AllergyDTO fromAllergyToAllergyDto(Allergy allergy) {
        return new AllergyDTO(allergy.getTitle());
    }
}

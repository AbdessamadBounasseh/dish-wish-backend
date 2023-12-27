package uit.ensak.dishwishbackend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.dto.AllergyDTO;
import uit.ensak.dishwishbackend.mapper.AllergyMapper;
import uit.ensak.dishwishbackend.model.Allergy;
import uit.ensak.dishwishbackend.repository.AllergyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AllergyService {
    private final AllergyRepository allergyRepository;
    private final AllergyMapper allergyMapper;
    public List<AllergyDTO> getAllAllergies() {
        log.info("Fetching all allergies");
        List<Allergy> allergies = allergyRepository.findAll();
        return allergies.stream()
                .map(allergyMapper::fromAllergyToAllergyDto)
                .collect(Collectors.toList());
    }
}

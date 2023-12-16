package uit.ensak.dishwishbackend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.dto.DietDTO;
import uit.ensak.dishwishbackend.mapper.DietMapper;
import uit.ensak.dishwishbackend.model.Diet;
import uit.ensak.dishwishbackend.repository.DietRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class DietService {
    private final DietRepository dietRepository;
    private  final DietMapper dietMapper;
    public List<DietDTO> getAllDiets() {
        log.info("Fetching all diets");
        List<Diet> diets = dietRepository.findAll();
        return diets.stream()
                .map(dietMapper::fromDietToDietDto)
                .collect(Collectors.toList());
    }
}

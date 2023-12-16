package uit.ensak.dishwishbackend.controller;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uit.ensak.dishwishbackend.service.AllergyService;

@RestController
@RequestMapping("allergies")
@AllArgsConstructor
@Transactional
public class AllergyController {
    private final AllergyService allergyService;
    @GetMapping("/all")
    public ResponseEntity<?> getAllDiets(){
        return ResponseEntity.ok(this.allergyService.getAllAllergies());
    }
}

package uit.ensak.dishwishbackend.controller;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uit.ensak.dishwishbackend.service.DietService;

@RestController
@Transactional
@RequestMapping("diets")
@AllArgsConstructor
@Slf4j
public class DietController {
    private final DietService dietService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllDiets(){
        return ResponseEntity.ok(this.dietService.getAllDiets());
    }
}

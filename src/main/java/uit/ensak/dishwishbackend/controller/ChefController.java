package uit.ensak.dishwishbackend.controller;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.service.ChefService;

@RestController
@AllArgsConstructor
@Transactional
@RequestMapping("/chefs")
public class ChefController {
    private final ChefService chefService;

    @GetMapping("/{chefId}")
    public ResponseEntity<Chef> getClientById(@PathVariable Long chefId) throws ClientNotFoundException {
        Chef chef = chefService.getChefById(chefId);
        return new ResponseEntity<>(chef, HttpStatus.OK);
    }
}

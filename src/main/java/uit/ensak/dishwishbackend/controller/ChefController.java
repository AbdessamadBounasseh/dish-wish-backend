package uit.ensak.dishwishbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.dto.ChefDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.mapper.ChefMapper;
import uit.ensak.dishwishbackend.model.Allergy;
import uit.ensak.dishwishbackend.model.Diet;
import uit.ensak.dishwishbackend.service.ChefService;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/chefs")
public class ChefController {
    private final ChefService chefService;

    private final ChefMapper chefMapper;

    public ChefController(ChefService chefService, ChefMapper chefMapper) {
        this.chefService = chefService;
        this.chefMapper = chefMapper;
    }

    @PutMapping(value = "/update/{chefId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateClient(@PathVariable long chefId, @RequestPart("chef") ChefDTO chefDTO,
                                               @RequestPart("photo") MultipartFile photo) throws IOException {
        String response = this.chefService.updateChef(chefId, chefMapper.fromChefDTO(chefDTO), photo);

        if(response == "OK") {
            return ResponseEntity.status(HttpStatus.OK).body("Cook updated successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Not allowed extension");
        }
    }

    @PutMapping("/update/allergies/{chefId}")
    public ResponseEntity<String> updateChefAllergies(@PathVariable long chefId, @RequestPart("allergies") List<Allergy> allergies) throws ClientNotFoundException {
        this.chefService.updateChefAllergies(chefId, allergies);
        return ResponseEntity.status(HttpStatus.OK).body("User allergies updated successfully");
    }

    @PutMapping("/update/diets/{chefId}")
    public ResponseEntity<String> updateChefDiets(@PathVariable long chefId, @RequestPart("diets") List<Diet> diets) throws ClientNotFoundException {
        this.chefService.updateChefDiets(chefId, diets);
        return ResponseEntity.status(HttpStatus.OK).body("User diets updated successfully");
    }

    @PostMapping(value = "/certificate/{chefId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> handleCertificate(@PathVariable long chefId, @RequestBody MultipartFile certificate) throws ClientNotFoundException, IOException {
        String response = this.chefService.handleCertificate(chefId,certificate);
        if(response == "OK") {
            return ResponseEntity.status(HttpStatus.OK).body("Certificate submitted successfully");
        }
        else if(response == "Not allowed extension") {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Not allowed extension");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client Not Found");
        }
    }
    @DeleteMapping(value = "/delete/{chefId}")
    public ResponseEntity<String> deleteClientAccount(@PathVariable long chefId) {
        this.chefService.deleteChefAccount(chefId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Cook deleted successfully");
    }

}

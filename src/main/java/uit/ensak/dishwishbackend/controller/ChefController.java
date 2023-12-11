package uit.ensak.dishwishbackend.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.dto.ChefDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.mapper.ChefMapper;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.service.ChefService;

import java.io.IOException;

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

    @GetMapping("/{chefId}")
    public ResponseEntity<Chef> getChefById(@PathVariable long chefId) throws ClientNotFoundException {
        Chef chef = chefService.getChefById(chefId);
        return ResponseEntity.ok(chef);
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

    @PostMapping(value = "/certificate/{chefId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> handleCertificate(@PathVariable long chefId, @RequestBody MultipartFile certificate) throws ClientNotFoundException, IOException {
        String response = this.chefService.handleCertificate(chefId,certificate);
        if(response == "OK") {
            return ResponseEntity.status(HttpStatus.OK).body("Certificate submitted successfully");
        }
        else if(response == "Not allowed extension") {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Not allowed extension for Certificate");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client Not Found");
        }
    }
    @PostMapping(value = "/idCard/{chefId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> handleIdCard(@PathVariable long chefId, @RequestBody MultipartFile idCard) throws ClientNotFoundException, IOException{
        String response = this.chefService.handleIdCard(chefId,idCard);
        if(response == "OK") {
            return ResponseEntity.status(HttpStatus.OK).body("idCard submitted successfully");
        }
        else if(response == "Not allowed extension") {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Not allowed extension for idCard");
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

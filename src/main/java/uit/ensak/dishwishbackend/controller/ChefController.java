package uit.ensak.dishwishbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.dto.ChefDTO;
import uit.ensak.dishwishbackend.mapper.ChefMapper;
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

    @PutMapping(value = "/update/{chefId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateClient(@PathVariable long chefId, @RequestPart("chef") ChefDTO chefDTO,
                                               @RequestPart("photo") MultipartFile photo) throws IOException {
        this.chefService.updateChef(chefId, chefMapper.fromChefDTO(chefDTO), photo);
        return ResponseEntity.status(HttpStatus.OK).body("Cook updated successfully");
    }

    @DeleteMapping(value = "/delete/{chefId}")
    public ResponseEntity<String> deleteClientAccount(@PathVariable long chefId) {
        this.chefService.deleteChefAccount(chefId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Cook deleted successfully");
    }

}

package uit.ensak.dishwishbackend.controller;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.InvalidFileExtensionException;
import uit.ensak.dishwishbackend.service.ChefService;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@AllArgsConstructor
@Transactional
@RequestMapping("/chefs")
public class ChefController {
    private final ChefService chefService;

    @PostMapping(value = "/certificate/{chefId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> handleCertificate(@PathVariable long chefId, @RequestBody MultipartFile certificate){
        try {
            this.chefService.handleCertificate(chefId,certificate);
            return ResponseEntity.ok("Certificate submitted successfully");
        } catch (ClientNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Error during client's certificate saving");
        } catch (InvalidFileExtensionException e) {
            return ResponseEntity.status(UNSUPPORTED_MEDIA_TYPE).body(e.getMessage());
        }
    }

    @PostMapping(value = "/idCard/{chefId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> handleIdCard(@PathVariable long chefId, @RequestBody MultipartFile idCard) {
        try {
            this.chefService.handleIdCard(chefId,idCard);
            return ResponseEntity.ok("idCard submitted successfully");
        } catch (ClientNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Error during client's idCard saving");
        } catch (InvalidFileExtensionException e) {
            return ResponseEntity.status(UNSUPPORTED_MEDIA_TYPE).body(e.getMessage());
        }
    }
}

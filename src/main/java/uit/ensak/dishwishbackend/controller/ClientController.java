package uit.ensak.dishwishbackend.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.dto.ChefDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.service.ClientService;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@Transactional
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable Long clientId) throws ClientNotFoundException {
        Client client = clientService.getClientById(clientId);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }
    @GetMapping("/email")
    public ResponseEntity<Client> getClientByEmail(@RequestBody String email) throws ClientNotFoundException {
        Client client = clientService.getClientByEmail(email);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        clientService.saveClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @PutMapping(value = "/update/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateClient(@PathVariable long id, @RequestPart("user") ChefDTO userDTO,
                                          @RequestPart("photo") MultipartFile photo) throws ClientNotFoundException, IOException {
            Client updateUser = clientService.updateUser(id, userDTO, photo);
            return ResponseEntity.ok(updateUser);
    }
    @PostMapping(value = "/becomeCook/{clientId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Chef> becomeCook(@PathVariable Long clientId,
                                                    @RequestPart("idCard") MultipartFile idCard,
                                                    @RequestPart("certificate") MultipartFile certificate) throws ClientNotFoundException, IOException {

            Chef chef = this.clientService.becomeCook(clientId, "CHEF",idCard, certificate);
            return ResponseEntity.ok(chef);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteUserAccount(@PathVariable long id) throws ClientNotFoundException {
        this.clientService.deleteUserAccount(id);
        return ResponseEntity.status(NO_CONTENT).body("User deleted successfully");
    }
}

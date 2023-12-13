package uit.ensak.dishwishbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.dto.ClientDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.mapper.ClientMapper;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.service.ClientService;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    public ClientController(ClientService clientService, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable Long clientId) throws ClientNotFoundException {
        Client client = clientService.getClientById(clientId);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createClient(@RequestBody Client client) {
        clientService.saveClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @PutMapping(value = "/update/{clientId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateClient(@PathVariable long chefId, @RequestPart("client") ClientDTO clientDTO,
                                               @RequestPart("photo") MultipartFile photo) throws IOException {
        String response = this.clientService.updateClient(chefId, clientMapper.fromClientDTO(clientDTO), photo);
        if(response.equals("OK")) {
            return ResponseEntity.status(HttpStatus.OK).body("Client updated successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Not allowed extension");
        }
    }

    @DeleteMapping(value = "/delete/{clientId}")
    public ResponseEntity<String> deleteClientAccount(@PathVariable long clientId) {
        this.clientService.deleteClientAccount(clientId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
    }

    @PutMapping("/{clientId}/switch-role")
    public ResponseEntity<String> switchRole(@PathVariable Long clientId) {
        try {
            clientService.switchRole(clientId);
            return ResponseEntity.ok("Role switched successfully");
        } catch (ClientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

package uit.ensak.dishwishbackend.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.repository.ClientRepository;


import java.util.List;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Transactional
@Slf4j
public class ClientService implements IClientSevice {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClientById(Long id) throws ClientNotFoundException {
        log.info("Fetching user by id {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client by Id " + id + " could not be found."));
    }

    public Client saveClient(Client client) {
        log.info("Saving new client {}", client);
        return clientRepository.save(client);
    }

    @Override
    public Client getClientByEmail(String email) {
        log.info("Fetching client by email {}", email);
        return clientRepository.findClientByEmail(email).orElseThrow();
    }

    @Override
    public List<Client> getAllClients() {
        log.info("Fetching all clients");
        return clientRepository.findAll();
    }

    public void updateClient(long clientId, Client updateClient, MultipartFile photo) throws IOException {
            updateClient.setId(clientId);
        String basePath = "src/main/resources/images/profilePhotos/";
        updateClient.setPhoto(this.saveFile(clientId, photo, basePath));
            clientRepository.save(updateClient);

    }

    private String saveFile(long id, MultipartFile file,String basePath) throws IOException {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == "default-profile-pic-dishwish" ) {
            return basePath + "default-profile-pic-dishwish";
        }else {

            File existingFile = new File(basePath + originalFileName);
            if (existingFile.exists()) {
                return basePath + originalFileName;
            }
            else {
                String filename = String.valueOf(id) + "_" + originalFileName;
                String filePath = "src/main/resources/images/profilePhotos/" + filename;
                Files.write(Paths.get(filePath), file.getBytes());
                return filePath;
            }
        }
    }
}

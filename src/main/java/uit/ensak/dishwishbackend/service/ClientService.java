package uit.ensak.dishwishbackend.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.VerificationToken;
import uit.ensak.dishwishbackend.repository.ClientRepository;
import uit.ensak.dishwishbackend.repository.TokenRepository;
import uit.ensak.dishwishbackend.model.Diet;
import uit.ensak.dishwishbackend.repository.AllergyRepository;
import uit.ensak.dishwishbackend.repository.DietRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
@Slf4j
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    private final TokenRepository tokenRepository;

    private final AllergyRepository allergyRepository;
    private final DietRepository dietRepository;

    public ClientService(ClientRepository clientRepository, AllergyRepository allergyRepository, DietRepository dietRepository, TokenRepository tokenRepository) {
        this.clientRepository = clientRepository;
        this.allergyRepository = allergyRepository;
        this.dietRepository = dietRepository;
        this.tokenRepository = tokenRepository;
    }

    public Client getClientById(long id) throws ClientNotFoundException {
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

    @Override
    public void saveUserVerificationToken(Client client, String token) {
        UUID uuid = UUID.randomUUID();
        String code = uuid.toString().replaceAll("-", "").substring(0, 6);
        var verificationToken = new VerificationToken(client, token, code);
        tokenRepository.save(verificationToken);
    }
  
    public String updateClient(long clientId, Client updateClient, MultipartFile photo) throws IOException {
        log.info("Updating user of id {} ", clientId);
        updateClient.setId(clientId);

        String basePath = "src/main/resources/images/profilePhotos/";
        String[] allowedExtensions = {"jpg", "jpeg", "png"};

        if(this.saveFile(clientId,photo,basePath,allowedExtensions)!=null) {
            updateClient.setPhoto(this.saveFile(clientId, photo, basePath, allowedExtensions));
            clientRepository.save(updateClient);
            return "OK";
        }
        else{
            return "Not allowed extension";
        }
    }

    public void deleteClientAccount(long clientId) {
        log.info("Deleting client of id {} ", clientId);
        this.clientRepository.deleteById(clientId);
    }

    private String saveFile(long id, MultipartFile file, String basePath, String[] allowedExtensions) throws IOException {

        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);

        log.info("Saving user of id {}", +id + " file : ", originalFileName);

        if (Arrays.asList(allowedExtensions).contains(fileExtension.toLowerCase())){
            if (originalFileName == "default-profile-pic-dishwish") {
                return basePath + "default-profile-pic-dishwish";
            } else {

                File existingFile = new File(basePath + originalFileName);
                if (existingFile.exists()) {
                    return basePath + originalFileName;
                } else {
                    String filename = String.valueOf(id) + "_" + originalFileName;
                    String filePath = basePath + filename;
                    Files.write(Paths.get(filePath), file.getBytes());
                    return filePath;
                }
            }
        }
        else {
            return null;
        }

    }

}

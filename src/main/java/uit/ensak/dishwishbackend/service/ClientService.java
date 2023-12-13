package uit.ensak.dishwishbackend.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Allergy;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.VerificationToken;
import uit.ensak.dishwishbackend.repository.ClientRepository;
import uit.ensak.dishwishbackend.repository.TokenRepository;
import uit.ensak.dishwishbackend.model.Diet;
import uit.ensak.dishwishbackend.repository.AllergyRepository;
import uit.ensak.dishwishbackend.repository.DietRepository;

import java.util.Arrays;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    private final TokenRepository tokenRepository;

     public ClientService(ClientRepository clientRepository, TokenRepository tokenRepository) {
        this.clientRepository = clientRepository;
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

    @Override
    public void revokeAllUserTokens(Client client) {
        var validUserTokens = tokenRepository
                .findAllValidTokenByUser(client.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
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
        String fileExtension = null;
        if (originalFileName != null) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        }

        log.info("Saving user of id {} file {} ",id, originalFileName);

        if (fileExtension != null && Arrays.asList(allowedExtensions).contains(fileExtension.toLowerCase())){
            if (originalFileName.equals("default-profile-pic-dish-wish")) {
                return basePath + "default-profile-pic-dish-wish";
            } else {

                File existingFile = new File(basePath + originalFileName);
                if (existingFile.exists()) {
                    return basePath + originalFileName;
                } else {
                    String filename = id + "_" + originalFileName;
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


    @Transactional
    public void switchRole(Long clientId) throws ClientNotFoundException {
        Client client = getClientById(clientId);

        if (client instanceof Chef) {
            // If the client is a chef, switch to client
            Client updatedClient = new Client();
            copyFields(client, updatedClient);
            //updatedClient.setId(clientId);
            clientRepository.save(updatedClient);
        } else if (client instanceof Client) {
            // If the client is a regular client, switch to chef
            Chef updatedChef = new Chef();
            copyFields(client, updatedChef);
            //updatedChef.setId(clientId);
            clientRepository.save(updatedChef);
        }

        clientRepository.delete(client);
    }

    private void copyFields(Client source, Client target) {
        // Copy common fields from source to target
        target.setEmail(source.getEmail());
        target.setPassword(source.getPassword());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setAddress(source.getAddress());
        target.setPhoneNumber(source.getPhoneNumber());
        target.setPhoto(source.getPhoto());

        // Check if source is a Chef before copying Chef-specific fields
        if (source instanceof Chef && target instanceof Chef) {
            Chef sourceChef = (Chef) source;
            Chef targetChef = (Chef) target;
            targetChef.setBio(sourceChef.getBio());
            targetChef.setIdCard(sourceChef.getIdCard());
            targetChef.setCertificate(sourceChef.getCertificate());
        }
    }





}

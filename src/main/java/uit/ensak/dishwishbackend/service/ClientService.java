package uit.ensak.dishwishbackend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.dto.ChefDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.InvalidFileExtensionException;
import uit.ensak.dishwishbackend.mapper.ChefMapper;
import uit.ensak.dishwishbackend.mapper.ClientMapper;
import uit.ensak.dishwishbackend.model.*;
import uit.ensak.dishwishbackend.repository.ClientRepository;
import uit.ensak.dishwishbackend.repository.TokenRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    private final TokenRepository tokenRepository;
    private final ClientMapper clientMapper;
    private final ChefMapper chefMapper;



    public Client getClientById(long id) throws ClientNotFoundException {
        log.info("Fetching user by id {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("User by Id " + id + " could not be found."));
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

    public Client updateUser(long id, ChefDTO updateUserDTO, MultipartFile photo) throws IOException, ClientNotFoundException {
        log.info("Updating user of id {} and {}", id, updateUserDTO);
        Client updateUser =this.getClientById(id);
        this.deleteOldDietsAndAllergiesAssoc(updateUser);
        if (updateUser instanceof Chef) {
            updateUser = chefMapper.fromChefDtoToChef(updateUserDTO,(Chef) updateUser);
        }
        else{
            updateUser = clientMapper.fromClientDtoToClient(updateUserDTO,updateUser);
        }

        String basePath = "src/main/resources/images/profilePhotos/";
        String[] allowedExtensions = {"jpg", "jpeg", "png"};
        String savingPhotoResponse = this.saveFile(id,photo,basePath,allowedExtensions);

        updateUser.setPhoto(savingPhotoResponse);
        return updateUser;
    }
    public void deleteOldDietsAndAllergiesAssoc(Client updateUser){
        for (Diet diet : updateUser.getDiets()) {
            diet.getClients().remove(updateUser);
        }
        for (Allergy allergy : updateUser.getAllergies()) {
            allergy.getClients().remove(updateUser);
        }
        updateUser.getDiets().clear();
        updateUser.getAllergies().clear();
    }

    public void deleteUserAccount(long id) {
        log.info("Deleting User of id {} ", id);
        List<VerificationToken> tokens = tokenRepository.findAllByClientId(id);
        tokenRepository.deleteAll(tokens);
        this.clientRepository.deleteById(id);
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
            }
            else {
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
            throw new InvalidFileExtensionException("Not Allowed Extension");
        }

    }
}

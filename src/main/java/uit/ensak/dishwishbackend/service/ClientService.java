package uit.ensak.dishwishbackend.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ClientService implements IClientService {
    @PersistenceContext
    private EntityManager entityManager;
    private final ClientRepository clientRepository;
    private final TokenRepository tokenRepository;
    private final ClientMapper clientMapper;
    private final ChefMapper chefMapper;
    private final ChefService chefService;

    public Client getClientById(Long id) throws ClientNotFoundException {
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
                .findAllValidTokenByUserId(client.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void deleteOldDietsAndAllergiesAssoc(Client client) {
        for (Diet diet : client.getDiets()) {
            diet.getClients().remove(client);
        }
        for (Allergy allergy : client.getAllergies()) {
            allergy.getClients().remove(client);
        }
        client.getDiets().clear();
        client.getAllergies().clear();
    }
    public Client updateUser(Long id, ChefDTO updateUserDTO, MultipartFile photo) throws IOException, ClientNotFoundException {
        log.info("Updating user of id {} and {}", id, updateUserDTO);
        Client updateUser = this.getClientById(id);
        this.deleteOldDietsAndAllergiesAssoc(updateUser);
        if (updateUser instanceof Chef) {
            updateUser = chefMapper.fromChefDtoToChef(updateUserDTO, (Chef) updateUser);
        } else {
            updateUser = clientMapper.fromClientDtoToClient(updateUserDTO, updateUser);
        }

        String basePath = "src/main/resources/images/profilePhotos/";
        String savingPhotoResponse = this.saveImage(id, photo, basePath);
        System.out.println("hello");

        updateUser.setPhoto(savingPhotoResponse);
        return updateUser;
    }

    public Client addRoleAndTypeToClient(Long id, String roleName) throws ClientNotFoundException {
        log.info("Adding role {} to user by id{}", roleName, id);

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client by id " + id + " could not be found."));
        Role role = Role.valueOf(roleName);
        client.setRole(role);
        client.setTYPE("CHEF");
        clientRepository.changeClientType(client.getId());
        return clientRepository.save(client);
    }

    public Chef becomeCook(Long id, String roleName, MultipartFile idCard, MultipartFile certificate) throws ClientNotFoundException, IOException {
        if (verifyImageExtension(idCard) && verifyImageExtension(certificate)) {
            Client client = addRoleAndTypeToClient(id, roleName);
            Long chefId = client.getId();
            entityManager.clear();
            Chef chef = chefService.getChefById(chefId);
            chef.setIdCard(chefService.handleIdCard(chef, idCard));
            chef.setCertificate(chefService.handleCertificate(chef, certificate));
            return chefService.saveChef(chef);
        }
        else{
        throw new InvalidFileExtensionException("Not Allowed Extension for idCard or Certificate");
        }
}

public void deleteUserAccount(Long id) throws ClientNotFoundException {
    Optional<Client> client = clientRepository.findById(id);
    if(client.isPresent()){
    log.info("Deleting User of id {} ", id);
    List<VerificationToken> tokens = tokenRepository.findAllByClientId(id);
    tokenRepository.deleteAll(tokens);
    this.clientRepository.deleteById(id);
    }
    else {
        throw new ClientNotFoundException("User by Id " + id + " could not be found.");
    }
}

public boolean verifyImageExtension(MultipartFile image) {
    String originalImageName = image.getOriginalFilename();
    String[] allowedExtensions = {"jpg", "jpeg", "png"};
    String imageExtension = null;
    if (originalImageName != null) {
        imageExtension = originalImageName.substring(originalImageName.lastIndexOf('.') + 1);
    }
    return imageExtension != null && Arrays.asList(allowedExtensions).contains(imageExtension.toLowerCase());
}

public String saveImage(Long id, MultipartFile image, String basePath) throws IOException {
    String originalImageName = image.getOriginalFilename();
    log.info("Saving user of id {} file {} ", id, originalImageName);

    if (verifyImageExtension(image)) {
        if (originalImageName != null && originalImageName.contains("default-profile-pic-dish-wish")) {
            return basePath + "default-profile-pic-dish-wish";
        } else {
            File existingImage = new File(basePath + originalImageName);
            if (existingImage.exists()) {
                return basePath + originalImageName;
            } else {
                String imageName = id + "_" + originalImageName;
                String imagePath = basePath + imageName;
                Files.write(Paths.get(imagePath), image.getBytes());
                return imagePath;
            }
        }
    } else {
        throw new InvalidFileExtensionException("Not Allowed Extension");
    }
}
}

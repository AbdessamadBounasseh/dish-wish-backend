package uit.ensak.dishwishbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.repository.ChefRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

@Service
@Slf4j
public class ChefService {
    private final ChefRepository chefRepository;


    public ChefService(ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }


    public Chef getChefById(long id) throws ClientNotFoundException {
        return chefRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("chef by Id " + id + " could not be found."));
    }

    public void saveChef(Chef chef) {
        log.info("Saving cook {} ", chef);
        chefRepository.save(chef);
    }

    public String updateChef(long chefId, Chef updateChef, MultipartFile photo) throws IOException {
        log.info("Updating cook of id {} ", chefId);
        updateChef.setId(chefId);
        String basePath = "src/main/resources/images/profilePhotos/";
        String[] allowedExtensions = {"jpg", "jpeg", "png"};

        if (this.saveFile(chefId, photo, basePath, allowedExtensions) != null) {
            updateChef.setPhoto(this.saveFile(chefId, photo, basePath, allowedExtensions));
            chefRepository.save(updateChef);
            return "OK";
        } else {
            return "Not allowed extension";
        }
    }
    public String handleCertificate(long chefId, MultipartFile certificate) throws ClientNotFoundException, IOException {
        Optional<Chef> optionalChef = Optional.ofNullable(this.getChefById(chefId));
        if (optionalChef.isPresent()) {
            Chef chef = optionalChef.get();
            String basePath = "src/main/resources/images/certificates/";
            String[] allowedExtensions = {"jpg", "jpeg", "png", "pdf"};

            if (this.saveFile(chefId, certificate, basePath, allowedExtensions) != null) {
                chef.setCertificate(this.saveFile(chefId, certificate, basePath, allowedExtensions));
                return "OK";
            } else {
                return "Not allowed extension";
            }
        } else {
            return "Cook Not Found";
        }
    }

    public String handleIdCard(long chefId, MultipartFile idCard) throws ClientNotFoundException, IOException {
        Optional<Chef> optionalChef = Optional.ofNullable(this.getChefById(chefId));
        if (optionalChef.isPresent()) {
            Chef chef = optionalChef.get();
            String basePath = "src/main/resources/images/idCards/";
            String[] allowedExtensions = {"jpg", "jpeg", "png"};

            if (this.saveFile(chefId, idCard, basePath, allowedExtensions) != null) {
                chef.setCertificate(this.saveFile(chefId, idCard, basePath, allowedExtensions));
                return "OK";
            } else {
                return "Not allowed extension";
            }
        } else {
            return "Cook Not Found";
        }
    }

    public void deleteChefAccount(long chefId) {
        log.info("Deleting cook of id {} ", chefId);
        this.chefRepository.deleteById(chefId);
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
}
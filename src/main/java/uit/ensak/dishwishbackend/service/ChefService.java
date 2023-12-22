package uit.ensak.dishwishbackend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.InvalidFileExtensionException;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.repository.ChefRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Service
@AllArgsConstructor
@Slf4j
public class ChefService {
    private final ChefRepository chefRepository;

    public Chef getChefById(long id) throws ClientNotFoundException {
        return chefRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Cook by Id " + id + " could not be found."));
    }

    public Chef saveChef(Chef chef) {
        log.info("Saving new cook {}", chef);
        return chefRepository.save(chef);
    }

    public void handleCertificate(Chef chef, MultipartFile certificate) throws IOException {
        String basePath = "src/main/resources/images/certificates/";
        String saveFileResponse = this.saveFile(chef.getId(), certificate, basePath);
        chef.setCertificate(saveFileResponse);
    }

    public void handleIdCard(Chef chef, MultipartFile idCard) throws IOException {
        String basePath = "src/main/resources/images/idCards/";
        String saveFileResponse = this.saveFile(chef.getId(), idCard, basePath);
        chef.setIdCard(saveFileResponse);
    }

    public boolean verifyFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String[] allowedExtensions = {"jpg", "jpeg", "png"};
        String fileExtension = null;
        if (originalFileName != null) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        }
        return fileExtension != null && Arrays.asList(allowedExtensions).contains(fileExtension.toLowerCase());
    }

    private String saveFile(long id, MultipartFile file, String basePath) throws IOException {
        String originalFileName = file.getOriginalFilename();
        log.info("Saving user of id {} file {} ", id, originalFileName);

        if (verifyFileExtension(file)) {
            if (originalFileName != null && originalFileName.equals("default-profile-pic-dish-wish")) {
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
        } else {
            throw new InvalidFileExtensionException("Not Allowed Extension");
        }
    }
}
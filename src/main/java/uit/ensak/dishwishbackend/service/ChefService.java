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

    public void handleCertificate(long chefId, MultipartFile certificate) throws ClientNotFoundException, IOException {
        Chef chef = getChefById(chefId);
        String basePath = "src/main/resources/images/certificates/";
        String[] allowedExtensions = {"jpg", "jpeg", "png", "pdf"};

        String saveFileResponse = this.saveFile(chefId, certificate, basePath, allowedExtensions);
        chef.setCertificate(saveFileResponse);
        this.chefRepository.save(chef);
    }

    public void handleIdCard(long chefId, MultipartFile idCard) throws ClientNotFoundException, IOException {
        Chef chef = getChefById(chefId);
        String basePath = "src/main/resources/images/idCards/";
        String[] allowedExtensions = {"jpg", "jpeg", "png"};

        String saveFileResponse = this.saveFile(chefId, idCard, basePath, allowedExtensions);
        chef.setIdCard(saveFileResponse);
        this.chefRepository.save(chef);
    }

    private String saveFile(long id, MultipartFile file, String basePath, String[] allowedExtensions) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = null;
        if (originalFileName != null) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        }

        log.info("Saving user of id {} file {} ", id, originalFileName);

        if (fileExtension != null && Arrays.asList(allowedExtensions).contains(fileExtension.toLowerCase())) {
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
        } else {
            throw new InvalidFileExtensionException("Not Allowed Extension");
        }

    }
}
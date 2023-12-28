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

    public Chef getChefById(Long id) throws ClientNotFoundException {
        return chefRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Cook by Id " + id + " could not be found."));
    }

    public Chef saveChef(Chef chef) {
        log.info("Saving new cook {}", chef);
        return chefRepository.save(chef);
    }

    public String handleCertificate(Chef chef, MultipartFile certificate) throws IOException {
        String basePath = "src/main/resources/images/certificates/";
        return this.saveImage(chef.getId(), certificate, basePath);
    }

    public String handleIdCard(Chef chef, MultipartFile idCard) throws IOException {
        String basePath = "src/main/resources/images/idCards/";
        return this.saveImage(chef.getId(), idCard, basePath);
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

    public String saveImage(long id, MultipartFile image, String basePath) throws IOException {
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
package uit.ensak.dishwishbackend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.dto.ChefDTO;
import uit.ensak.dishwishbackend.dto.ChefDetailsDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.InvalidFileExtensionException;
import uit.ensak.dishwishbackend.mapper.ChefMapper;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Comment;
import uit.ensak.dishwishbackend.repository.ChefRepository;
import uit.ensak.dishwishbackend.repository.CommentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ChefService {
    private final ChefRepository chefRepository;
    private final ChefMapper chefMapper;
    private final CommentRepository commentRepository;

    public Chef getChefById(Long id) throws ClientNotFoundException {
        return chefRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Cook by Id " + id + " could not be found."));
    }
    public ChefDetailsDTO getChefDetails(Long id) throws ClientNotFoundException {
        Chef chef = chefRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Cook by Id " + id + " could not be found."));
        List<Comment> comments = commentRepository.findByReceiverId(id);

        return ChefDetailsDTO.builder().chef(chef).comments(comments).build();
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

    public List<ChefDTO> filterChefByName(String query){
        List <Chef> chefs = new ArrayList<>();
        String[] queryWords = query.split("\\s+");
        for (String word : queryWords){
            chefs.addAll(chefRepository.findByFirstNameContaining(word));
            chefs.addAll(chefRepository.findByLastNameContaining(word));
        }
        return chefs.stream()
                .map(chefMapper::fromChefToChefDto)
                .collect(Collectors.toList());
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
package uit.ensak.dishwishbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.repository.ChefRepository;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ChefService {
    public ChefService(ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }

    private final ChefRepository chefRepository;


        public Chef getChefById(long id) throws ClientNotFoundException {
            return chefRepository.findById(id)
                    .orElseThrow(() -> new ClientNotFoundException("chef by Id "+ id +" could not be found."));
        }

        public void saveChef(Chef chef) {
            chefRepository.save(chef);
        }

        public void updateChef(long chefId, Chef updateChef, MultipartFile photo) throws IOException {
            updateChef.setId(chefId);
            String basePath = "src/main/resources/images/profilePhotos/";
            updateChef.setPhoto(this.saveFile(chefId, photo, basePath));
            chefRepository.save(updateChef);
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
package uit.ensak.dishwishbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.repository.ClientRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
@Service
public class ClientService implements IClientSevice {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClientById(long id) throws ClientNotFoundException {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client by Id "+ id +" could not be found."));
    }

    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    public void updateClient(long clientId, Client updateClient, MultipartFile photo) throws IOException {
            updateClient.setId(clientId);
        String basePath = "src/main/resources/images/profilePhotos/";
        updateClient.setPhoto(this.saveFile(clientId, photo, basePath));
            clientRepository.save(updateClient);

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

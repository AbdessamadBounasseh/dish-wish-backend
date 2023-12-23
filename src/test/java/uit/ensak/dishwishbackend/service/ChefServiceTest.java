package uit.ensak.dishwishbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.InvalidFileExtensionException;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.repository.ChefRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChefServiceTest {
    @Mock
    private ChefRepository chefRepository;
    @InjectMocks
    private ChefService chefService;

    @Test
    void ChefService_GetChefById_ReturnChef() throws ClientNotFoundException {
        Long chefId = 1L;
        Chef chef = new Chef();
        chef.setId(chefId);

        when(chefRepository.findById(chefId)).thenReturn(Optional.of(chef));

        Chef returnChef = chefService.getChefById(chefId);

        assertNotNull(returnChef);
        assertEquals(chefId, returnChef.getId());
    }

    @Test
    void ChefService_GetChefById_ThrowsClientNotFoundException() {
        Long chefId = 1L;

        when(chefRepository.findById(chefId)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> chefService.getChefById(chefId));
    }

    @Test
    void ChefService_SaveChef_ReturnChef() {
        Long chefId = 1L;
        Chef chef = new Chef();
        chef.setId(chefId);

        when(chefRepository.save(chef)).thenReturn(chef);

        chefService.saveChef(chef);

        verify(chefRepository).save(chef);
    }

    @Test
    void ChefService_SaveChef_FailedBecauseOfDuplicatedIds() {
        Long chefId = 1L;

        Chef chef1 = new Chef();
        chef1.setId(chefId);
        Chef chef2 = new Chef();
        chef2.setId(chefId);

        doThrow(DataIntegrityViolationException.class)
                .when(chefRepository).save(chef2);
        assertThrows(DataIntegrityViolationException.class,
                () -> chefService.saveChef(chef2));
    }

    @Test
    void ChefService_handleIdCard_ReturnString() throws IOException {
        MockMultipartFile idCard = new MockMultipartFile("idCard.jpg",
                "idCard.jpg", "image/jpg", "idCard".getBytes());
        Long chefId = 1L;
        Chef chef = new Chef();
        chef.setId(chefId);

        String returnPath = chefService.handleIdCard(chef, idCard);
        Path path = Paths.get("src/main/resources/images/idCards/1_idCard.jpg");
        Files.deleteIfExists(path);

        Assertions.assertEquals("src/main/resources/images/idCards/1_idCard.jpg", returnPath);
    }

    @Test
    void ChefService_handleCertificate_ReturnString() throws IOException {
        MockMultipartFile certificate = new MockMultipartFile("cert.jpg",
                "cert.jpg", "image/jpg", "cert".getBytes());
        Long chefId = 1L;
        Chef chef = new Chef();
        chef.setId(chefId);

        String returnPath = chefService.handleCertificate(chef, certificate);
        Path path = Paths.get("src/main/resources/images/certificates/1_cert.jpg");
        Files.deleteIfExists(path);

        Assertions.assertEquals("src/main/resources/images/certificates/1_cert.jpg", returnPath);
    }

    @Test
    void ChefService_handleIdCard_ThrowsInvalidFileExtensionException() {
        MockMultipartFile idCard = new MockMultipartFile("idCard.gif",
                "idCard.gif", "image/gif", "idCard".getBytes());
        Long chefId = 1L;
        Chef chef = new Chef();
        chef.setId(chefId);
        assertThrows(InvalidFileExtensionException.class,
                () -> chefService.handleIdCard(chef, idCard));
    }

    @Test
    void ChefService_handleCertificate_ThrowsInvalidFileExtensionException() {
        MockMultipartFile certificate = new MockMultipartFile("cert.gif",
                "cert.gif", "image/gif", "cert".getBytes());
        Long chefId = 1L;
        Chef chef = new Chef();
        chef.setId(chefId);
        assertThrows(InvalidFileExtensionException.class,
                () -> chefService.handleCertificate(chef, certificate));
    }

    @Test
    public void ChefService_VerifyImageExtension_ReturnTrue() {
        MockMultipartFile image = new MockMultipartFile("test.jpg",
                "test.jpg", "image/jpg", "test".getBytes());
        assertTrue(chefService.verifyImageExtension(image));
    }

    @Test
    public void ChefService_VerifyImageExtension_ReturnFalse() {
        MockMultipartFile image = new MockMultipartFile("test.gif",
                "test.gif", "image/gif", "test".getBytes());
        assertFalse(chefService.verifyImageExtension(image));
    }

    @Test
    public void ChefService_SaveImage_WithValidExtension_ReturnPath() throws IOException {
        long id = 1L;
        String basePath = "src/test/resources/images/";
        MultipartFile image = new MockMultipartFile("test.jpg",
                "test.jpg", "image/jpg", "test".getBytes());

        String imagePath = chefService.saveImage(id, image, basePath);
        Path path = Paths.get(imagePath);
        Files.deleteIfExists(path);

        Assertions.assertEquals(basePath + "1_test.jpg", imagePath);
    }

    @Test
    public void ChefService_SaveImage_PresentImage_WithValidExtension_ReturnPath() throws IOException {
        long id = 1L;
        String basePath = "src/test/resources/images/";
        MultipartFile image = new MockMultipartFile("test2.png",
                "test2.png", "image/png", "test2".getBytes());

        String imagePath = chefService.saveImage(id, image, basePath);
        Assertions.assertEquals(basePath + "1_test2.png", imagePath);
    }

    @Test
    public void ChefService_SaveImage_WithInvalidExtension_ThrowsInvalidFileExtensionException() {
        long id = 1L;
        String basePath = "src/test/resources/images/";
        MockMultipartFile image = new MockMultipartFile("test.gif",
                "test.gif", "image/gif", "test".getBytes());

        assertThrows(InvalidFileExtensionException.class,
                () -> chefService.saveImage(id, image, basePath));
    }

    @Test
    public void ChefService_SaveImage_WithValidExtension_ReturnDefaultPath() throws IOException {
        long id = 1L;
        String basePath = "src/test/resources/images/";
        String defaultPath = basePath + "default-profile-pic-dish-wish";
        MultipartFile image = new MockMultipartFile("default-profile-pic-dish-wish.jpg",
                "default-profile-pic-dish-wish.jpg", "image/jpg", "default-profile-pic-dish-wish".getBytes());

        String imagePath = chefService.saveImage(id, image, basePath);

        Assertions.assertEquals(defaultPath, imagePath);
    }
}

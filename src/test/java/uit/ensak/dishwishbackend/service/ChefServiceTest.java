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
import uit.ensak.dishwishbackend.dto.ChefDTO;
import uit.ensak.dishwishbackend.dto.ChefDetailsDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.InvalidFileExtensionException;
import uit.ensak.dishwishbackend.mapper.ChefMapper;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Comment;
import uit.ensak.dishwishbackend.repository.ChefRepository;
import uit.ensak.dishwishbackend.repository.CommentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChefServiceTest {
    @Mock
    private ChefRepository chefRepository;
    @Mock
    private ChefMapper chefMapper;
    @Mock
    private CommentRepository commentRepository;
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
    public void ChefService_GetChefDetails_ReturnChefDetailsDTO() throws ClientNotFoundException {
        Chef chef = mock(Chef.class);
        Comment comment1 = mock(Comment.class);
        Comment comment2 = mock(Comment.class);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);

        when(chefRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(chef));
        when(commentRepository.findByReceiverId(any(Long.class))).thenReturn(comments);

        ChefDetailsDTO returnChefDetailsDTO = chefService.getChefDetails(1L);

        Assertions.assertNotNull(returnChefDetailsDTO);
        Assertions.assertEquals(chef, returnChefDetailsDTO.getChef());
        Assertions.assertEquals(2, returnChefDetailsDTO.getComments().size());
    }
    @Test
    public void ChefService_GetChefDetails_ThrowsClientNotFoundException()  {

        when(chefRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class,
                () -> chefService.getChefDetails(1L));
    }

    @Test
    public void ChefService_FilterChefByNameAndAddress_ReturnListOfChefDTO(){
        Chef chef1 = mock(Chef.class);
        Chef chef2 = mock(Chef.class);
        Chef chef3 = mock(Chef.class);
        Chef chef4 = mock(Chef.class);
        Chef chef5 = mock(Chef.class);
        Chef chef6 = mock(Chef.class);
        ChefDTO chefDTO1 = mock(ChefDTO.class);
        ChefDTO chefDTO2 = mock(ChefDTO.class);
        ChefDTO chefDTO3 = mock(ChefDTO.class);
        ChefDTO chefDTO4 = mock(ChefDTO.class);
        ChefDTO chefDTO5 = mock(ChefDTO.class);
        ChefDTO chefDTO6 = mock(ChefDTO.class);
        List<Chef> chefs1 = new ArrayList<>();
        List<Chef> chefs2 = new ArrayList<>();
        List<Chef> chefs3 = new ArrayList<>();
        chefs1.add(chef1);
        chefs1.add(chef2);
        chefs2.add(chef3);
        chefs2.add(chef4);
        chefs3.add(chef5);
        chefs3.add(chef6);

        when(chefRepository.findByFirstNameContaining(anyString())).thenReturn(chefs1);
        when(chefRepository.findByLastNameContaining(anyString())).thenReturn(chefs2);
        when(chefRepository.findByAddressContaining(anyString())).thenReturn(chefs3);
        when(chefMapper.fromChefToChefDto(any(Chef.class)))
                .thenReturn(chefDTO1).thenReturn(chefDTO2).thenReturn(chefDTO3)
                .thenReturn(chefDTO4).thenReturn(chefDTO5).thenReturn(chefDTO6);

        List<ChefDTO> returnChefsDTO = chefService.filterChefByNameAndAddress("fay");

        Assertions.assertEquals(6,returnChefsDTO.size());
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

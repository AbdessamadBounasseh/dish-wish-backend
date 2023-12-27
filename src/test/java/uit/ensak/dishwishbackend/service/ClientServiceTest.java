package uit.ensak.dishwishbackend.service;

import jakarta.persistence.EntityManager;
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
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.InvalidFileExtensionException;
import uit.ensak.dishwishbackend.mapper.ChefMapper;
import uit.ensak.dishwishbackend.mapper.ClientMapper;
import uit.ensak.dishwishbackend.model.*;
import uit.ensak.dishwishbackend.repository.ClientRepository;
import uit.ensak.dishwishbackend.repository.TokenRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private EntityManager entityManager;
    @Mock
    private ChefService chefService;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private ChefMapper chefMapper;
    @InjectMocks
    private ClientService clientService;

    @Test
    void ClientService_getClientById_succeed() throws ClientNotFoundException {
        // given
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // when
        Client retrievedClient = clientService.getClientById(clientId);

        // then
        assertNotNull(retrievedClient);
        assertEquals(clientId, retrievedClient.getId());
    }

    @Test
    void ClientService_getClientById_ThrowsClientNotFoundException() {
        // given
        Long clientId = 1L;

        // when
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // then
        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(clientId));
    }

    @Test
    void ClientService_saveClient_succeed() {
        // given
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);

        // when
        clientService.saveClient(client);

        // then
        verify(clientRepository).save(client);
    }

    @Test
    void ClientService_saveClient_FailedBecauseOfDuplicatedIds() {
        // given
        Long clientId = 1L;

        Client client1 = new Client();
        client1.setId(clientId);

        Client client2 = new Client();
        client2.setId(clientId);

        doThrow(DataIntegrityViolationException.class)
                .when(clientRepository).save(client2);

        // when & then
        assertThrows(DataIntegrityViolationException.class,
                () -> clientService.saveClient(client2));
    }

    @Test
    public void ClientService_GetClientByEmail_ReturnClient() {
        Client client = mock(Client.class);
        String email = "dishwish@gmail.com";

        when(clientRepository.findClientByEmail(anyString())).thenReturn(Optional.ofNullable(client));

        Client returnClient = clientService.getClientByEmail(email);
        Assertions.assertEquals(client, returnClient);
    }

    @Test
    public void ClientService_GetClientByEmail_ThrowsException() {
        String email = "dishwish@gmail.com";

        when(clientRepository.findClientByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> clientService.getClientByEmail(email));
    }

    @Test
    public void ClientService_addRoleAndTypeToClient_ReturnClient() throws ClientNotFoundException {
        Long id = 1L;
        String roleName = "CHEF";
        Client client = Client.builder().id(1L).build();

        when(clientRepository.findById(id)).thenReturn(Optional.ofNullable(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        doNothing().when(clientRepository).changeClientType(any(Long.class));

        Client returnClient = clientService.addRoleAndTypeToClient(id, roleName);

        Assertions.assertEquals(1L, returnClient.getId());
        Assertions.assertEquals("CHEF", returnClient.getTYPE());
        Assertions.assertEquals("CHEF", returnClient.getRole().name());
    }

    @Test
    public void ClientService_addRoleAndTypeToClient_ThrowsClientNotFoundException() {
        Long id = 1L;
        String roleName = "CHEF";

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.addRoleAndTypeToClient(id, roleName));
    }

    @Test
    public void ClientService_BecomeCook_ReturnChef() throws ClientNotFoundException, IOException {
        MultipartFile idCard = new MockMultipartFile("idCard.jpg",
                "idCard.jpg", "image/jpg", "idCard".getBytes());
        MultipartFile certificate = new MockMultipartFile("cert.jpg",
                "cert.jpg", "image/jpg", "cert".getBytes());
        Long id = 1L;
        String roleName = "CHEF";
        Client client = Client.builder().id(id).build();
        Chef chef = new Chef();
        chef.setId(id);

        when(clientRepository.findById(id)).thenReturn(Optional.ofNullable(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        doNothing().when(clientRepository).changeClientType(any(Long.class));

        when(chefService.saveChef(any(Chef.class))).thenReturn(chef);
        when(chefService.getChefById(id)).thenReturn(chef);
        when(chefService.handleCertificate(any(Chef.class), eq(certificate)))
                .thenReturn("src/main/resources/images/certificates/1_cert.jpg");
        when(chefService.handleIdCard(any(Chef.class), eq(idCard)))
                .thenReturn("src/main/resources/images/idCards/1_idCard.jpg");

        Chef returnChef = clientService.becomeCook(id, roleName, idCard, certificate);
        Path path1 = Paths.get("src/main/resources/images/certificates/1_cert.jpg");
        Files.deleteIfExists(path1);
        Path path2 = Paths.get("src/main/resources/images/idCards/1_idCard.jpg");
        Files.deleteIfExists(path2);

        Assertions.assertNotNull(returnChef);
        Assertions.assertEquals("src/main/resources/images/certificates/1_cert.jpg", returnChef.getCertificate());
        Assertions.assertEquals("src/main/resources/images/idCards/1_idCard.jpg", returnChef.getIdCard());
    }

    @Test
    public void ClientService_BecomeCook_ThrowsClientNotFoundException() {
        MultipartFile idCard = new MockMultipartFile("idCard.jpg",
                "idCard.jpg", "image/jpg", "idCard".getBytes());
        MultipartFile certificate = new MockMultipartFile("cert.jpg",
                "cert.jpg", "image/jpg", "cert".getBytes());
        Long id = 1L;
        String roleName = "CHEF";

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.becomeCook(id, roleName, idCard, certificate));
    }

    @Test
    public void ClientService_BecomeCook_ThrowsInvalidFileExtensionException() {
        MultipartFile idCard = new MockMultipartFile("idCard.gif",
                "idCard.gif", "image/gif", "idCard".getBytes());
        MultipartFile certificate = new MockMultipartFile("cert.jpg",
                "cert.jpg", "image/jpg", "cert".getBytes());
        Long id = 1L;
        String roleName = "CHEF";

        assertThrows(InvalidFileExtensionException.class, () -> clientService.becomeCook(id, roleName, idCard, certificate));
    }

    @Test
    public void ClientService_UpdateUser_ReturnClient() throws IOException, ClientNotFoundException {
        MultipartFile image = new MockMultipartFile("test.jpg",
                "test.jpg", "image/jpg", "test".getBytes());
        Long id = 1L;
        ChefDTO updateUserDTO = new ChefDTO();
        updateUserDTO.setFirstName("nash");
        updateUserDTO.setLastName("Omega");
        updateUserDTO.setDietsDTO(new ArrayList<>());
        updateUserDTO.setAllergiesDTO(new ArrayList<>());
        Client updateUser = new Client();
        updateUser.setId(id);
        updateUser.setDiets(new ArrayList<>());
        updateUser.setAllergies(new ArrayList<>());
        Client updateUserConverted = Client.builder().id(1L).firstName("nash").lastName("omega").photo("").build();

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.of(updateUser));
        when(clientMapper.fromClientDtoToClient(updateUserDTO, updateUser)).thenReturn(updateUserConverted);

        Client returnUser = clientService.updateUser(id, updateUserDTO, image);
        Path path = Paths.get("src/main/resources/images/profilePhotos/1_test.jpg");
        Files.deleteIfExists(path);

        Assertions.assertInstanceOf(Client.class, returnUser);
        Assertions.assertEquals("src/main/resources/images/profilePhotos/1_test.jpg", returnUser.getPhoto());
    }

    @Test
    public void ClientService_UpdateUser_ReturnChef() throws IOException, ClientNotFoundException {
        MultipartFile image = new MockMultipartFile("test.jpg",
                "test.jpg", "image/jpeg", "test".getBytes());
        Long id = 1L;
        ChefDTO updateUserDTO = new ChefDTO();
        updateUserDTO.setFirstName("nash");
        updateUserDTO.setLastName("Omega");
        updateUserDTO.setBio("i'm a cook");
        updateUserDTO.setAllergiesDTO(new ArrayList<>());
        updateUserDTO.setDietsDTO(new ArrayList<>());
        Chef updateUser = new Chef();
        updateUser.setDiets(new ArrayList<>());
        updateUser.setAllergies(new ArrayList<>());
        updateUser.setId(id);
        Chef updateUserConverted = new Chef();
        updateUserConverted.setId(1L);
        updateUserConverted.setFirstName("nash");
        updateUserConverted.setLastName("Omega");
        updateUserConverted.setBio("i'm a cook");

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.of(updateUser));
        when(chefMapper.fromChefDtoToChef(updateUserDTO, updateUser)).thenReturn(updateUserConverted);

        Client returnUser = clientService.updateUser(id, updateUserDTO, image);

        Path path = Paths.get("src/main/resources/images/profilePhotos/1_test.jpg");
        Files.deleteIfExists(path);

        Assertions.assertInstanceOf(Chef.class, returnUser);
        Assertions.assertEquals("src/main/resources/images/profilePhotos/1_test.jpg", returnUser.getPhoto());
    }

    @Test
    public void ClientService_UpdateUser_ThrowsInvalidFileExtensionException() {
        Long id = 1L;
        ChefDTO updateUserDTO = new ChefDTO();
        updateUserDTO.setAllergiesDTO(new ArrayList<>());
        updateUserDTO.setDietsDTO(new ArrayList<>());
        MultipartFile image = new MockMultipartFile("test.gif",
                "test.gif", "image/gif", "test".getBytes());
        Client updateUser = new Client();
        updateUser.setDiets(new ArrayList<>());
        updateUser.setAllergies(new ArrayList<>());
        updateUser.setId(id);
        Client updateUserConverted = Client.builder().id(1L).photo("").build();

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.of(updateUser));
        when(clientMapper.fromClientDtoToClient(updateUserDTO, updateUser)).thenReturn(updateUserConverted);

        assertThrows(InvalidFileExtensionException.class, () -> clientService.updateUser(id, updateUserDTO, image));
    }

    @Test
    public void ClientService_UpdateUser_ThrowsClientNotFoundException() {
        Long id = 1L;
        ChefDTO updateUserDTO = new ChefDTO();
        MultipartFile image = new MockMultipartFile("test.jpg",
                "test.jpg", "image/jpg", "test".getBytes());
        Client updateUser = new Client();
        updateUser.setId(id);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.updateUser(id, updateUserDTO, image));
    }

    @Test
    public void ClientService_deleteOldDietsAndAllergiesAssoc_ReturnVoid() {
        Diet diet1 = Diet.builder().clients(new ArrayList<>()).build();
        Diet diet2 = Diet.builder().clients(new ArrayList<>()).build();
        Allergy allergy1 = Allergy.builder().clients(new ArrayList<>()).build();
        Allergy allergy2 = Allergy.builder().clients(new ArrayList<>()).build();
        Client client = Client.builder().diets(new ArrayList<>()).allergies(new ArrayList<>()).build();

        diet1.getClients().add(client);
        diet2.getClients().add(client);
        allergy1.getClients().add(client);
        allergy2.getClients().add(client);

        client.getDiets().add(diet1);
        client.getDiets().add(diet2);
        client.getAllergies().add(allergy1);
        client.getAllergies().add(allergy2);

        clientService.deleteOldDietsAndAllergiesAssoc(client);

        assertThat(diet1.getClients()).isEmpty();
        assertThat(diet2.getClients()).isEmpty();
        assertThat(allergy1.getClients()).isEmpty();
        assertThat(allergy2.getClients()).isEmpty();
        assertThat(client.getDiets()).isEmpty();
        assertThat(client.getAllergies()).isEmpty();
    }

    @Test
    public void ClientService_deleteUserAccount_ReturnVoid() {
        Long clientId = 1L;
        Client user = Client.builder().id(1L).build();
        VerificationToken token = new VerificationToken();
        token.setClient(user);
        when(clientRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        doReturn(Collections.singletonList(token)).when(tokenRepository).findAllByClientId(clientId);
        doNothing().when(tokenRepository).deleteAll(any());
        assertAll(() -> clientService.deleteUserAccount(clientId));
    }

    @Test
    public void ClientService_VerifyImageExtension_ReturnTrue() {
        MockMultipartFile image = new MockMultipartFile("test.jpg",
                "test.jpg", "image/jpg", "test".getBytes());
        assertTrue(clientService.verifyImageExtension(image));
    }

    @Test
    public void ClientService_VerifyImageExtension_ReturnFalse() {
        MockMultipartFile image = new MockMultipartFile("test.gif",
                "test.gif", "image/gif", "test".getBytes());
        assertFalse(clientService.verifyImageExtension(image));
    }

    @Test
    public void ClientService_SaveImage_WithValidExtension_ReturnPath() throws IOException {
        long id = 1L;
        String basePath = "src/test/resources/images/";
        MultipartFile image = new MockMultipartFile("test.jpg",
                "test.jpg", "image/jpg", "test".getBytes());

        String imagePath = clientService.saveImage(id, image, basePath);
        Path path = Paths.get(imagePath);
        Files.deleteIfExists(path);

        Assertions.assertEquals(basePath + "1_test.jpg", imagePath);
    }

    @Test
    public void ClientService_SaveImage_PresentImage_WithValidExtension_ReturnPath() throws IOException {
        long id = 1L;
        String basePath = "src/test/resources/images/";
        MultipartFile image = new MockMultipartFile("test2.png",
                "test2.png", "image/png", "test2".getBytes());

        String imagePath = clientService.saveImage(id, image, basePath);
        Assertions.assertEquals(basePath + "1_test2.png", imagePath);
    }

    @Test
    public void ClientService_SaveImage_WithInvalidExtension_ThrowsInvalidFileExtensionException() {
        long id = 1L;
        String basePath = "src/test/resources/images/";
        MockMultipartFile image = new MockMultipartFile("test.gif",
                "test.gif", "image/gif", "test".getBytes());

        assertThrows(InvalidFileExtensionException.class,
                () -> clientService.saveImage(id, image, basePath));
    }

    @Test
    public void ClientService_SaveImage_WithValidExtension_ReturnDefaultPath() throws IOException {
        long id = 1L;
        String basePath = "src/test/resources/images/";
        String defaultPath = basePath + "default-profile-pic-dish-wish";
        MultipartFile image = new MockMultipartFile("default-profile-pic-dish-wish.jpg", "default-profile-pic-dish-wish.jpg", "image/jpeg", "default-profile-pic-dish-wish".getBytes());

        String imagePath = clientService.saveImage(id, image, basePath);

        Assertions.assertEquals(defaultPath, imagePath);
    }
}
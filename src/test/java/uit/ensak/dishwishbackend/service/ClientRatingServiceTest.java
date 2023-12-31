package uit.ensak.dishwishbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uit.ensak.dishwishbackend.dto.RatingDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.ClientRating;
import uit.ensak.dishwishbackend.model.Rating;
import uit.ensak.dishwishbackend.repository.ChefRepository;
import uit.ensak.dishwishbackend.repository.ClientRepository;
import uit.ensak.dishwishbackend.repository.RatingRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientRatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ChefRepository chefRepository;

    @InjectMocks
    private ClientRatingService clientRatingService;

    @Test
    void getClientRatings_ValidClientId_ReturnListOfRatings() throws ClientNotFoundException {
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(ratingRepository.findByClientAndType(client, "CLIENT_RATING")).thenReturn(Collections.emptyList());

        List<Rating> ratings = clientRatingService.getClientRatings(clientId);

        assertNotNull(ratings);
        assertTrue(ratings.isEmpty());
    }

    @Test
    void getClientRatings_InvalidClientId_ThrowsClientNotFoundException() {
        Long invalidClientId = 999L;

        when(clientRepository.findById(invalidClientId)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientRatingService.getClientRatings(invalidClientId));
    }

    @Test
    void addRatingToClient_ValidRatingDetails_ReturnsClientRating() throws ClientNotFoundException {
        RatingDTO ratingDetails = new RatingDTO();
        ratingDetails.setClientId(1L);
        ratingDetails.setChefId(2L);
        ratingDetails.setRating(4.5);

        Client client = new Client();
        Chef chef = new Chef();

        when(clientRepository.findById(ratingDetails.getClientId())).thenReturn(Optional.of(client));
        when(chefRepository.findById(ratingDetails.getChefId())).thenReturn(Optional.of(chef));
        when(ratingRepository.save(any(ClientRating.class))).thenReturn(new ClientRating());

        ClientRating clientRating = clientRatingService.addRatingToClient(ratingDetails);

        assertNotNull(clientRating);
    }

    @Test
    void addRatingToClient_InvalidClientId_ThrowsClientNotFoundException() {
        RatingDTO ratingDetails = new RatingDTO();
        ratingDetails.setClientId(999L); // Invalid client ID

        when(clientRepository.findById(ratingDetails.getClientId())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientRatingService.addRatingToClient(ratingDetails));
    }

    @Test
    void addRatingToClient_InvalidChefId_ThrowsClientNotFoundException() {
        RatingDTO ratingDetails = new RatingDTO();
        ratingDetails.setClientId(1L);
        ratingDetails.setChefId(999L); // Invalid chef ID

        Client client = new Client();
        when(clientRepository.findById(ratingDetails.getClientId())).thenReturn(Optional.of(client));
        when(chefRepository.findById(ratingDetails.getChefId())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientRatingService.addRatingToClient(ratingDetails));
    }
}


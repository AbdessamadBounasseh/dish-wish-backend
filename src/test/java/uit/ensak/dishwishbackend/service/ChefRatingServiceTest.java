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
import uit.ensak.dishwishbackend.model.ChefRating;
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
class ChefRatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ChefRepository chefRepository;

    @InjectMocks
    private ChefRatingService chefRatingService;

    @Test
    void getChefRatings_ValidChefId_ReturnListOfRatings() throws ClientNotFoundException {
        Long chefId = 1L;
        Chef chef = new Chef();
        chef.setId(chefId);

        when(chefRepository.findById(chefId)).thenReturn(Optional.of(chef));
        when(ratingRepository.findByChefAndType(chef, "CHEF_RATING")).thenReturn(Collections.emptyList());

        List<Rating> ratings = chefRatingService.getChefRatings(chefId);

        assertNotNull(ratings);
        assertTrue(ratings.isEmpty());
    }

    @Test
    void getChefRatings_InvalidChefId_ThrowsClientNotFoundException() {
        Long invalidChefId = 999L;

        when(chefRepository.findById(invalidChefId)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> chefRatingService.getChefRatings(invalidChefId));
    }

    @Test
    void addRatingToChef_ValidRatingDetails_ReturnsChefRating() throws ClientNotFoundException {
        RatingDTO ratingDetails = new RatingDTO();
        ratingDetails.setClientId(1L);
        ratingDetails.setChefId(2L);
        ratingDetails.setRating(4.5);

        Client client = new Client();
        Chef chef = new Chef();

        when(clientRepository.findById(ratingDetails.getClientId())).thenReturn(Optional.of(client));
        when(chefRepository.findById(ratingDetails.getChefId())).thenReturn(Optional.of(chef));
        when(ratingRepository.save(any(ChefRating.class))).thenReturn(new ChefRating());

        ChefRating chefRating = chefRatingService.addRatingToChef(ratingDetails);

        assertNotNull(chefRating);
    }

    @Test
    void addRatingToChef_InvalidClientId_ThrowsClientNotFoundException() {
        RatingDTO ratingDetails = new RatingDTO();
        ratingDetails.setClientId(999L); // Invalid client ID

        when(clientRepository.findById(ratingDetails.getClientId())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> chefRatingService.addRatingToChef(ratingDetails));
    }

    @Test
    void addRatingToChef_InvalidChefId_ThrowsClientNotFoundException() {
        RatingDTO ratingDetails = new RatingDTO();
        ratingDetails.setClientId(1L);
        ratingDetails.setChefId(999L); // Invalid chef ID

        Client client = new Client();
        when(clientRepository.findById(ratingDetails.getClientId())).thenReturn(Optional.of(client));
        when(chefRepository.findById(ratingDetails.getChefId())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> chefRatingService.addRatingToChef(ratingDetails));
    }
}

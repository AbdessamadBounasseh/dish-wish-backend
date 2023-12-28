package uit.ensak.dishwishbackend.service;

import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.dto.RatingDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.ClientRating;
import uit.ensak.dishwishbackend.model.Rating;
import uit.ensak.dishwishbackend.repository.ChefRepository;
import uit.ensak.dishwishbackend.repository.ClientRepository;
import uit.ensak.dishwishbackend.repository.RatingRepository;

import java.util.List;

@Service
public class ClientRatingService implements IClientRatingService{
    private final RatingRepository ratingRepository;
    private final ClientRepository clientRepository;
    private final ChefRepository chefRepository;

    public ClientRatingService(RatingRepository ratingRepository,
                               ClientRepository clientRepository,
                               ChefRepository chefRepository) {
        this.ratingRepository = ratingRepository;
        this.clientRepository = clientRepository;
        this.chefRepository = chefRepository;
    }

    @Override
    public List<Rating> getClientRatings(Long clientId) throws ClientNotFoundException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("User by Id " + clientId + " could not be found."));
        return ratingRepository.findByClientAndType(client, "CLIENT_RATING");
    }

    @Override
    public ClientRating addRatingToClient(RatingDTO ratingDetails) throws ClientNotFoundException {
        Client client = clientRepository.findById(ratingDetails.getClientId())
                .orElseThrow(() -> new ClientNotFoundException("User by Id " + ratingDetails.getClientId() + " could not be found."));
        Chef chef = chefRepository.findById(ratingDetails.getChefId())
                .orElseThrow(() -> new ClientNotFoundException("Chef by Id " + ratingDetails.getChefId() + " could not be found."));

        ClientRating clientRating = new ClientRating();
        clientRating.setClient(client);
        clientRating.setChef(chef);
        clientRating.setRating(ratingDetails.getRating());

        return ratingRepository.save(clientRating);
    }
}

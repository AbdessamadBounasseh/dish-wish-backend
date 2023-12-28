package uit.ensak.dishwishbackend.service;

import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.dto.RatingDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.*;
import uit.ensak.dishwishbackend.repository.ChefRepository;
import uit.ensak.dishwishbackend.repository.ClientRepository;
import uit.ensak.dishwishbackend.repository.RatingRepository;

import java.util.List;

@Service
public class ChefRatingService implements IChefRatingService{
    private final RatingRepository ratingRepository;
    private final ClientRepository clientRepository;
    private final ChefRepository chefRepository;

    public ChefRatingService(RatingRepository ratingRepository,
                             ClientRepository clientRepository,
                             ChefRepository chefRepository) {
        this.ratingRepository = ratingRepository;
        this.clientRepository = clientRepository;
        this.chefRepository = chefRepository;
    }

    @Override
    public List<Rating> getChefRatings(Long chefId) throws ClientNotFoundException {
        Chef chef = chefRepository.findById(chefId)
                .orElseThrow(() -> new ClientNotFoundException("Chef by Id " + chefId + " could not be found."));

        return ratingRepository.findByChefAndType(chef, "CHEF_RATING");
    }

    @Override
    public ChefRating addRatingToChef(RatingDTO ratingDetails) throws ClientNotFoundException {
        Client client = clientRepository.findById(ratingDetails.getClientId())
                .orElseThrow(() -> new ClientNotFoundException("User by Id " + ratingDetails.getClientId() + " could not be found."));
        Chef chef = chefRepository.findById(ratingDetails.getChefId())
                .orElseThrow(() -> new ClientNotFoundException("Chef by Id " + ratingDetails.getChefId() + " could not be found."));

        ChefRating chefRating = new ChefRating();
        chefRating.setClient(client);
        chefRating.setChef(chef);
        chefRating.setRating(ratingDetails.getRating());

        return ratingRepository.save(chefRating);
    }
}

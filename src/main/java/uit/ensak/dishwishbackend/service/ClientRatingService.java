package uit.ensak.dishwishbackend.service;

import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.dto.ClientDTO;
import uit.ensak.dishwishbackend.dto.RatingDTO;
import uit.ensak.dishwishbackend.repository.RatingRepository;

import java.util.List;

@Service
public class ClientRatingService implements IClientRatingService{
    private final RatingRepository ratingRepository;

    public ClientRatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public List<Double> getClientRatings(Long clientId) {
        return null;
    }

    @Override
    public ClientDTO addRatingToClient(RatingDTO ratingDetails) {
        return null;
    }
}

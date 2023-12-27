package uit.ensak.dishwishbackend.service;

import uit.ensak.dishwishbackend.dto.ClientDTO;
import uit.ensak.dishwishbackend.dto.RatingDTO;

import java.util.List;

public interface IClientRatingService {
    List<Double> getClientRatings(Long clientId);

    ClientDTO addRatingToClient(RatingDTO ratingDetails);
}

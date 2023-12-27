package uit.ensak.dishwishbackend.service;

import uit.ensak.dishwishbackend.dto.ClientDTO;
import uit.ensak.dishwishbackend.dto.RatingDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;

import java.util.List;

public interface IClientRatingService {
    List<Double> getClientRatings(Long clientId) throws ClientNotFoundException;

    ClientDTO addRatingToClient(RatingDTO ratingDetails);
}

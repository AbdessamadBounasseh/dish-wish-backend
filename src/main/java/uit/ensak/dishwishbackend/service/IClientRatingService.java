package uit.ensak.dishwishbackend.service;

import uit.ensak.dishwishbackend.dto.ClientDTO;
import uit.ensak.dishwishbackend.dto.RatingDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.ClientRating;
import uit.ensak.dishwishbackend.model.Rating;

import java.util.List;

public interface IClientRatingService {
    List<Rating> getClientRatings(Long clientId) throws ClientNotFoundException;

    ClientRating addRatingToClient(RatingDTO ratingDetails) throws ClientNotFoundException;
}

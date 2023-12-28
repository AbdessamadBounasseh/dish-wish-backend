package uit.ensak.dishwishbackend.service;

import uit.ensak.dishwishbackend.dto.RatingDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.ChefRating;
import uit.ensak.dishwishbackend.model.ClientRating;
import uit.ensak.dishwishbackend.model.Rating;

import java.util.List;

public interface IChefRatingService {
    List<Rating> getChefRatings(Long chefId) throws ClientNotFoundException;

    ChefRating addRatingToChef(RatingDTO ratingDetails) throws ClientNotFoundException;
}

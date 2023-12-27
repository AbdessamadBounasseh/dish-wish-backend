package uit.ensak.dishwishbackend.service;

import uit.ensak.dishwishbackend.dto.ClientDTO;

public interface IRatingService {
    double getClientRatings(Long clientId);

    ClientDTO addRatingToClient(Long clientId, Long chefId, double rating);
}

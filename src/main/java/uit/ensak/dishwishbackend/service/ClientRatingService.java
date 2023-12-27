package uit.ensak.dishwishbackend.service;

import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.dto.ClientDTO;
import uit.ensak.dishwishbackend.dto.RatingDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.repository.ClientRepository;
import uit.ensak.dishwishbackend.repository.RatingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientRatingService implements IClientRatingService{
    private final RatingRepository ratingRepository;
    private final ClientRepository clientRepository;

    public ClientRatingService(RatingRepository ratingRepository,
                               ClientRepository clientRepository) {
        this.ratingRepository = ratingRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Double> getClientRatings(Long clientId) throws ClientNotFoundException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("User by Id " + clientId + " could not be found."));





    }

    @Override
    public ClientDTO addRatingToClient(RatingDTO ratingDetails) {
        return null;
    }
}

package uit.ensak.dishwishbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uit.ensak.dishwishbackend.dto.RatingDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.ClientRating;
import uit.ensak.dishwishbackend.model.Rating;
import uit.ensak.dishwishbackend.service.ClientRatingService;

import java.util.List;

@RestController
@RequestMapping("/chef-ratings")
public class ChefRatingController {
    private final ClientRatingService clientRatingService;

    public ChefRatingController(ClientRatingService clientRatingService) {
        this.clientRatingService = clientRatingService;
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<List<Rating>> getClientRatings(@PathVariable Long clientId) throws ClientNotFoundException {
        List<Rating> ratings = clientRatingService.getClientRatings(clientId);
        return ResponseEntity.ok(ratings);
    }

    @PostMapping("/rate")
    public ClientRating rateClient(@RequestBody RatingDTO ratingDetails) throws ClientNotFoundException {
        return clientRatingService.addRatingToClient(ratingDetails);
    }
}

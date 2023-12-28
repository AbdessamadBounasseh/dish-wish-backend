package uit.ensak.dishwishbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uit.ensak.dishwishbackend.dto.RatingDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.ChefRating;
import uit.ensak.dishwishbackend.model.ClientRating;
import uit.ensak.dishwishbackend.model.Rating;
import uit.ensak.dishwishbackend.service.ChefRatingService;
import uit.ensak.dishwishbackend.service.ClientRatingService;

import java.util.List;

@RestController
@RequestMapping("/chef-ratings")
public class ChefRatingController {
    private final ChefRatingService chefRatingService;

    public ChefRatingController(ChefRatingService chefRatingService) {
        this.chefRatingService = chefRatingService;
    }

    @GetMapping("/{chefId}")
    public ResponseEntity<List<Rating>> getChefRatings(@PathVariable Long chefId) throws ClientNotFoundException {
        List<Rating> ratings = chefRatingService.getChefRatings(chefId);
        return ResponseEntity.ok(ratings);
    }

    @PostMapping("/rate")
    public ChefRating rateChef(@RequestBody RatingDTO ratingDetails) throws ClientNotFoundException {
        return chefRatingService.addRatingToChef(ratingDetails);
    }
}

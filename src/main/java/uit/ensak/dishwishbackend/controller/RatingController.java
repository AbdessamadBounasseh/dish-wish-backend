package uit.ensak.dishwishbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uit.ensak.dishwishbackend.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

//    @GetMapping("/client")
//    public ResponseEntity<List<Integer>> getAllCommands() {
////        List<Command> commands = CommandService.getAllCommands();
////        return new ResponseEntity<>(commands, HttpStatus.OK);
//    }
}

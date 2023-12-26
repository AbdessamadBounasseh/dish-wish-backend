package uit.ensak.dishwishbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.CommandNotFoundException;
import uit.ensak.dishwishbackend.model.Command;
import uit.ensak.dishwishbackend.model.Proposition;
import uit.ensak.dishwishbackend.service.PropositionService;


@RestController
@RequestMapping("/propositions")
public class PropositionController {

    private final PropositionService propositionService;

    public PropositionController(PropositionService propositionService) {
        this.propositionService = propositionService;
    }


    @PostMapping("/offer")
    public ResponseEntity<Proposition> offerPriceForOrder(@RequestBody Proposition proposition ) throws ClientNotFoundException, CommandNotFoundException {

        Proposition createdProposition = propositionService.createProposition(proposition);
        if (createdProposition != null) {
            return new ResponseEntity<>(createdProposition, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}

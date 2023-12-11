package uit.ensak.dishwishbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.exception.CommandNotFoundException;
import uit.ensak.dishwishbackend.model.Command;
import uit.ensak.dishwishbackend.service.CommandService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/commands")
public class CommandController {

    @Autowired
    private CommandService CommandService;

    @GetMapping
    public ResponseEntity<List<Command>> getAllCommands() {
        List<Command> commands = CommandService.getAllCommands();
        return new ResponseEntity<>(commands, HttpStatus.OK);
    }

    @GetMapping("/{commandId}")
    public ResponseEntity<Command> getCommandDetails(@PathVariable Long commandId) throws CommandNotFoundException {
        Command command = CommandService.getCommandById(commandId);
        return new ResponseEntity<>(command, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Command> createCommand(@RequestBody Command command) {
        Command createdCommand = CommandService.createCommand(command);

        if (createdCommand != null) {
            return new ResponseEntity<>(createdCommand, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{commandId}/assign/{chefId}")
    public ResponseEntity<String> assignOrderToChef(@PathVariable Long commandId, @PathVariable Long chefId) {
        boolean assigned = CommandService.assignCommandToChef(commandId, chefId);
        if (assigned) {
            return new ResponseEntity<>("Order assigned to Chef", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to assign order", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{commandId}")
    public ResponseEntity<String> deleteCommand(@PathVariable Long commandId) {
        boolean deleted = CommandService.deleteCommand(commandId);
        if (deleted) {
            return new ResponseEntity<>("Order deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete order", HttpStatus.BAD_REQUEST);
        }
    }


}

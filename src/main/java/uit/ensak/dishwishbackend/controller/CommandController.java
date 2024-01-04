package uit.ensak.dishwishbackend.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uit.ensak.dishwishbackend.dto.ChefCommandHistoryDTO;
import uit.ensak.dishwishbackend.dto.ClientCommandHistoryDTO;
import uit.ensak.dishwishbackend.dto.CommandDTO;
import uit.ensak.dishwishbackend.exception.CommandNotFoundException;
import uit.ensak.dishwishbackend.model.Command;
import uit.ensak.dishwishbackend.service.CommandService;

import java.util.List;

@RestController
@RequestMapping("/commands")
@AllArgsConstructor
public class CommandController {

    private final CommandService commandService;

    @GetMapping
    public ResponseEntity<List<Command>> getAllCommands() {
        List<Command> commands = commandService.getAllCommands();
        return new ResponseEntity<>(commands, HttpStatus.OK);
    }

    @GetMapping("/{commandId}")
    public ResponseEntity<Command> getCommandDetails(@PathVariable Long commandId) throws CommandNotFoundException {
        Command command = commandService.getCommandById(commandId);
        return new ResponseEntity<>(command, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Command> createCommand(@RequestBody Command command) {
        Command createdCommand = commandService.createCommand(command);

        if (createdCommand != null) {
            return new ResponseEntity<>(createdCommand, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{commandId}/assign/{chefId}")
    public ResponseEntity<String> assignOrderToChef(@PathVariable Long commandId, @PathVariable Long chefId) {
        boolean assigned = commandService.assignCommandToChef(commandId, chefId);
        if (assigned) {
            return new ResponseEntity<>("Order assigned to Chef", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to assign order", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{commandId}")
    public ResponseEntity<String> deleteCommand(@PathVariable Long commandId) {
        boolean deleted = commandService.deleteCommand(commandId);
        if (deleted) {
            return new ResponseEntity<>("Order deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete order", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{commandId}")
    public ResponseEntity<?> updateCommand(@PathVariable Long commandId, @RequestBody CommandDTO commandDTO) throws CommandNotFoundException {
        Command updatedCommand = commandService.updateCommand(commandId, commandDTO);
        if (updatedCommand != null) {
            return new ResponseEntity<>(updatedCommand, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Command not found", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ClientCommandHistoryDTO> getClientCommandsHistory(@PathVariable Long clientId) {
        ClientCommandHistoryDTO commandsHistory = commandService.getClientCommandsHistory(clientId);
        return ResponseEntity.ok(commandsHistory);
    }

    @GetMapping("/chef/{chefId}")
    public ResponseEntity<ChefCommandHistoryDTO> getChefCommandsHistory(@PathVariable Long chefId) {
        ChefCommandHistoryDTO commandsHistory = commandService.getChefCommandsHistory(chefId);
        return ResponseEntity.ok(commandsHistory);
    }
}


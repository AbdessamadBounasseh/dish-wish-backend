package uit.ensak.dishwishbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.exception.CommandNotFoundException;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Command;
import uit.ensak.dishwishbackend.repository.CommandRepository;
import uit.ensak.dishwishbackend.repository.ChefRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommandService implements ICommandService {

    private final CommandRepository commandRepository;
    private final ChefRepository ChefRepository;

    @Autowired
    public CommandService(CommandRepository commandRepository, ChefRepository ChefRepository) {
        this.commandRepository = commandRepository;
        this.ChefRepository = ChefRepository;
    }

    public List<Command> getAllCommands() {
        return commandRepository.findAll();
    }

    public Command getCommandById(Long id)  throws CommandNotFoundException {
        return commandRepository.findById(id).orElseThrow(() -> new CommandNotFoundException("Command by Id "+ id +" could not be found."));
    }

    public Command createCommand(Command command) {
        return commandRepository.save(command);
    }

    public boolean deleteCommand(Long id) {
        if (commandRepository.existsById(id)) {
            commandRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean assignCommandToChef(Long commandId, Long chefId) {
        Optional<Command> commandOptional = commandRepository.findById(commandId);
        Optional<Chef> chefOptional = ChefRepository.findById(chefId);

        if (commandOptional.isPresent() && chefOptional.isPresent()) {
            Command command = commandOptional.get();
            Chef chef = chefOptional.get();

            // Assign the command to the chef (update the command entity)
            command.setChef(chef);
            // Save the updated command back to the repository
            commandRepository.save(command);

            return true;
        }
        return false;
    }

}

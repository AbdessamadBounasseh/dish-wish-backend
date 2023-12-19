package uit.ensak.dishwishbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.dto.CommandDTO;
import uit.ensak.dishwishbackend.exception.CommandNotFoundException;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Command;
import uit.ensak.dishwishbackend.repository.CommandRepository;
import uit.ensak.dishwishbackend.repository.ChefRepository;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.modelmapper.ModelMapper;

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

    public Command updateCommand(Long commandId, CommandDTO commandDTO) throws CommandNotFoundException {
        Command existingCommand = commandRepository.findById(commandId)
                .orElseThrow(() -> new CommandNotFoundException("Command by Id "+ commandId +" could not be found."));

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setPropertyCondition(context -> context.getSource() != null);

        modelMapper.map(commandDTO, existingCommand);
        return commandRepository.save(existingCommand);
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

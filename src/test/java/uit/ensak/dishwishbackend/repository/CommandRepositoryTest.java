package uit.ensak.dishwishbackend.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Command;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommandRepositoryTest {
    private final CommandRepository commandRepository;
    private final ClientRepository clientRepository;
    private final ChefRepository chefRepository;

    @Autowired
    public CommandRepositoryTest(CommandRepository commandRepository, ClientRepository clientRepository,ChefRepository chefRepository) {
        this.commandRepository = commandRepository;
        this.clientRepository = clientRepository;
        this.chefRepository = chefRepository;
    }

    @Test
    void commandRepository_FindByClientIdAndStatus_ReturnListOfCommands(){
        Client client =  Client.builder().id(1L).build();

        Command command1 = Command.builder().id(1L).client(client).status("IN_PROGRESS").build();
        Command command2 = Command.builder().id(2L).client(client).status("IN_PROGRESS").build();
        Command command3 = Command.builder().id(3L).client(client).status("FINISHED").build();
        Command command4 = Command.builder().id(4L).client(client).status("FINISHED").build();
        Command command5 = Command.builder().id(5L).client(client).status("FINISHED").build();

        clientRepository.save(client);
        commandRepository.save(command1);
        commandRepository.save(command2);
        commandRepository.save(command3);
        commandRepository.save(command4);
        commandRepository.save(command5);


        List<Command> clientCommandsInProgress = commandRepository.findByClientIdAndStatus(1L,"IN_PROGRESS");
        List<Command> clientCommandsFinished = commandRepository.findByClientIdAndStatus(1L,"FINISHED");

        Assertions.assertNotNull(clientCommandsInProgress);
        Assertions.assertNotNull(clientCommandsFinished);
        Assertions.assertEquals(2, clientCommandsInProgress.size());
        Assertions.assertEquals(3, clientCommandsFinished.size());
    }

    @Test
    void commandRepository_FindByChefIdAndStatus_ReturnListOfCommands(){

        Chef chef = new Chef() ;
        chef.setId(1L);
        Client client =  Client.builder().id(2L).build();


        Command command1 = Command.builder().id(1L).chef(chef).client(client).status("IN_PROGRESS").build();
        Command command2 = Command.builder().id(2L).chef(chef).client(client).status("IN_PROGRESS").build();
        Command command3 = Command.builder().id(3L).chef(chef).client(client).status("FINISHED").build();
        Command command4 = Command.builder().id(4L).chef(chef).client(client).status("FINISHED").build();
        Command command5 = Command.builder().id(5L).chef(chef).client(client).status("FINISHED").build();

        chefRepository.save(chef);
        clientRepository.save(client);
        commandRepository.save(command1);
        commandRepository.save(command2);
        commandRepository.save(command3);
        commandRepository.save(command4);
        commandRepository.save(command5);


        List<Command> chefCommandsInProgress = commandRepository.findByChefIdAndStatus(1L,"IN_PROGRESS");
        List<Command> chefCommandsFinished = commandRepository.findByChefIdAndStatus(1L,"FINISHED");

        Assertions.assertNotNull(chefCommandsInProgress);
        Assertions.assertNotNull(chefCommandsFinished);
        Assertions.assertEquals(2, chefCommandsInProgress.size());
        Assertions.assertEquals(3, chefCommandsFinished.size());
    }
}

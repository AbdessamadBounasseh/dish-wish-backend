package uit.ensak.dishwishbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uit.ensak.dishwishbackend.exception.CommandNotFoundException;
import uit.ensak.dishwishbackend.model.Command;
import uit.ensak.dishwishbackend.repository.CommandRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommandServiceTest {

    @Mock
    private CommandRepository commandRepository;

    @InjectMocks
    private CommandService commandService;

    @Test
    void testGetAllCommands_ReturnsListOfCommands() {
        // Given
        Command command1 = new Command();
        Command command2 = new Command();
        List<Command> mockCommands = Arrays.asList(command1, command2);
        when(commandRepository.findAll()).thenReturn(mockCommands);

        // When
        List<Command> result = commandService.getAllCommands();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(command1, result.get(0));
        assertEquals(command2, result.get(1));
    }

    @Test
    void testGetAllCommands_WhenRepositoryFails_ThrowsException() {
        // Given
        when(commandRepository.findAll()).thenThrow(new RuntimeException("Repository error"));

        // When/Then
        assertThrows(RuntimeException.class, () -> commandService.getAllCommands());
    }

    @Test
    void testGetCommandById_ExistingId_ReturnsCommand() throws CommandNotFoundException {
        // Given
        Long commandId = 1L;
        Command mockCommand = new Command();
        when(commandRepository.findById(commandId)).thenReturn(Optional.of(mockCommand));

        // When
        Command result = commandService.getCommandById(commandId);

        // Then
        assertNotNull(result);
        assertEquals(mockCommand, result);
    }

    @Test
    void testGetCommandById_NonExistingId_ThrowsException() {
        // Given
        Long nonExistingCommandId = 100L;
        when(commandRepository.findById(nonExistingCommandId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(CommandNotFoundException.class, () -> commandService.getCommandById(nonExistingCommandId));
    }


    @Test
    void testCreateCommand_ReturnsCommand() {
        // Given
        Command command = new Command();
        when(commandRepository.save(command)).thenReturn(command);

        // When
        Command result = commandService.createCommand(command);

        // Then
        assertNotNull(result);
        assertEquals(command, result);
    }

    @Test
    void testCreateCommand_CallsSave() {
        // Given
        Command command = new Command();

        // When
        commandService.createCommand(command);

        // Then
        verify(commandRepository, times(1)).save(any());// the save method of
        // commandRepository is called exactly once (times(1)) with any argument (any()).
    }

}

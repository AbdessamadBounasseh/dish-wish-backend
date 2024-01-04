package uit.ensak.dishwishbackend.dto;

import lombok.*;
import uit.ensak.dishwishbackend.model.Command;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientCommandHistoryDTO {
    private List<Command> CommandsInProgress;
    private List<Command> CommandsFinished;
}

package uit.ensak.dishwishbackend.dto;

import lombok.*;
import uit.ensak.dishwishbackend.model.Command;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChefCommandHistoryDTO {
    private List<Command> CommandsInProgressForMe;
    private List<Command> CommandsFinishedForMe;
    private List<Command> CommandsInProgressByMe;
    private List<Command> CommandsFinishedByMe;
}

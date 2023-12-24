package uit.ensak.dishwishbackend.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommandStatus {
    IN_PROGRESS("In progress"),
    FINISHED("Finished");

    @Getter
    private final String status;
}

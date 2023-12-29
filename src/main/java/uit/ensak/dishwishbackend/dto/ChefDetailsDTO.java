package uit.ensak.dishwishbackend.dto;

import lombok.*;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.model.Comment;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChefDetailsDTO {
    private Chef chef;
    private List<Comment> comments;
}

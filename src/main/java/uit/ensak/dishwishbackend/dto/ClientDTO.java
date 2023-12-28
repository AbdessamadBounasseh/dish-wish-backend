package uit.ensak.dishwishbackend.dto;

import lombok.*;
import uit.ensak.dishwishbackend.model.Rating;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDTO {

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private List<DietDTO> dietsDTO;

    private List<AllergyDTO> allergiesDTO;

    private List<Rating> ratings;
}

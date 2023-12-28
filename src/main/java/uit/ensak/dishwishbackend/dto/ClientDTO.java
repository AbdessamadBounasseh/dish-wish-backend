package uit.ensak.dishwishbackend.dto;

import lombok.*;

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

    private String allergies;

    private DietDTO dietDTO;
}

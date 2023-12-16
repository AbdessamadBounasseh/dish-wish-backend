package uit.ensak.dishwishbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClientDTO {

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private List<DietDTO> dietsDTO;

    private List<AllergyDTO> allergiesDTO;
}

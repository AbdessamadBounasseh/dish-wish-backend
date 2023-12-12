package uit.ensak.dishwishbackend.dto;


import lombok.Data;
import uit.ensak.dishwishbackend.model.Allergy;
import uit.ensak.dishwishbackend.model.Diet;

import java.util.List;

@Data
public class ClientDTO {

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private List<Diet> diets;

    private List<Allergy> allergies;


}

package uit.ensak.dishwishbackend.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class ClientDTO {

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private String photo;

    private Instant createdOn;

    private Instant lastUpdatedOn;

}

package uit.ensak.dishwishbackend.controller.auth.payloads;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    private String email;
    private String password;
}

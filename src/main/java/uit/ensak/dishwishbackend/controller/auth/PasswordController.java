package uit.ensak.dishwishbackend.controller.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uit.ensak.dishwishbackend.controller.auth.payloads.ChangePasswordRequest;
import uit.ensak.dishwishbackend.service.auth.AuthenticationService;

import java.security.Principal;

@RestController
@RequestMapping("/password")
@Slf4j
public class PasswordController {

    private final AuthenticationService authenticationService;

    public PasswordController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest passwordRequest,
                                            Principal connectedUser){
        authenticationService.changePassword(passwordRequest, connectedUser);
        return ResponseEntity.ok().build();
    }
}

package uit.ensak.dishwishbackend.controller.auth;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uit.ensak.dishwishbackend.controller.auth.payloads.ChangePasswordRequest;
import uit.ensak.dishwishbackend.service.PasswordService;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@RestController
@RequestMapping("/password")
@Slf4j
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest passwordRequest,
                                            Principal connectedUser){
        passwordService.changePassword(passwordRequest, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) throws MessagingException, UnsupportedEncodingException {
        passwordService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }
}

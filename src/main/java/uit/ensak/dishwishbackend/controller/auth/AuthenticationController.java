package uit.ensak.dishwishbackend.controller.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uit.ensak.dishwishbackend.service.auth.AuthenticationService;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        log.info("Received registration request: {}", request);
        AuthenticationResponse response = authenticationService.register(request);
        log.info("Registration successful for user: {}", response.getToken());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        log.info("Received authentication request for user: {}", request.getEmail());
        AuthenticationResponse response = authenticationService.authenticate(request);
        log.info("Authentication successful for user: {}", response.getToken());
        return ResponseEntity.ok().body(response);
    }
}

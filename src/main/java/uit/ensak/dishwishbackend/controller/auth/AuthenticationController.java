package uit.ensak.dishwishbackend.controller.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import uit.ensak.dishwishbackend.exception.VerificationTokenNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.VerificationToken;
import uit.ensak.dishwishbackend.security.JwtUtils;
import uit.ensak.dishwishbackend.service.ClientService;
import uit.ensak.dishwishbackend.service.auth.AuthenticationService;
import uit.ensak.dishwishbackend.service.auth.VerificationTokenService;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;
    private final ClientService clientService;
    private final JwtUtils jwtUtils;
    private final VerificationTokenService tokenService;

    public AuthenticationController(AuthenticationService authenticationService, UserDetailsService userDetailsService, ClientService clientService, JwtUtils jwtUtils, VerificationTokenService tokenService) {
        this.authenticationService = authenticationService;
        this.userDetailsService = userDetailsService;
        this.clientService = clientService;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        log.info("Received registration request: {}", request);
        AuthenticationResponse response = authenticationService.register(request);
        log.info("Registration successful for user: {}", response.getToken());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/register/verify-email")
    private ResponseEntity<String> verifyEmail(@RequestParam String code) throws VerificationTokenNotFoundException {
        String token = tokenService.getTokenByCode(code);
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(jwtUtils.extractUsername(token));
        Client client = clientService.getClientByEmail(userDetails.getUsername());
        if (client.isEnabled()) {
            return ResponseEntity.ok().body("Your account has already been verified, you can login !");
        } else if (jwtUtils.isTokenExpired(token)) {
            tokenService.deleteToken(token);
            return ResponseEntity.badRequest().body("Token already expired !");
        } else if (jwtUtils.isTokenValid(token, userDetails)) {
            client.setEnabled(true);
            clientService.saveClient(client);
            return ResponseEntity.ok().body("Your account has been successfully verified. " +
                    "Now you can login to your account !");
        } else {
            return ResponseEntity.badRequest().body("Invalid verification token !");
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        log.info("Received authentication request for user: {}", request.getEmail());
        AuthenticationResponse response = authenticationService.authenticate(request);
        log.info("Authentication successful for user: {}", response.getToken());
        return ResponseEntity.ok().body(response);
    }
}

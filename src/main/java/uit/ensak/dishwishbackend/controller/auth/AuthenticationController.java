package uit.ensak.dishwishbackend.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import uit.ensak.dishwishbackend.controller.auth.payloads.AuthenticationRequest;
import uit.ensak.dishwishbackend.controller.auth.payloads.AuthenticationResponse;
import uit.ensak.dishwishbackend.controller.auth.payloads.RegisterRequest;
import uit.ensak.dishwishbackend.exception.VerificationTokenNotFoundException;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.security.JwtService;
import uit.ensak.dishwishbackend.service.ClientService;
import uit.ensak.dishwishbackend.service.auth.AuthenticationService;
import uit.ensak.dishwishbackend.service.auth.EmailVerificationService;
import uit.ensak.dishwishbackend.service.auth.VerificationTokenService;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;
    private final ClientService clientService;
    private final JwtService jwtService;
    private final VerificationTokenService tokenService;
    private final EmailVerificationService verificationService;
    private final HttpServletRequest servletRequest;

    public AuthenticationController(AuthenticationService authenticationService, UserDetailsService userDetailsService, ClientService clientService, JwtService jwtService, VerificationTokenService tokenService, EmailVerificationService verificationService, HttpServletRequest servletRequest) {
        this.authenticationService = authenticationService;
        this.userDetailsService = userDetailsService;
        this.clientService = clientService;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.verificationService = verificationService;
        this.servletRequest = servletRequest;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        log.info("Received registration request: {}", request);
        AuthenticationResponse response = authenticationService.register(request);
        log.info("Registration successful for user: {}", response.getAccessToken());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register/verify-email")
    private ResponseEntity<String> verifyEmail(@RequestParam String code) throws VerificationTokenNotFoundException {
        String url = verificationService.applicationUrl(servletRequest)+"/auth/refresh-token";
        String token = tokenService.getTokenByCode(code);
        UserDetails userDetails = tokenService.getByToken(token).getClient();
        Client client = clientService.getClientByEmail(userDetails.getUsername());
        if (client.isEnabled()) {
            return ResponseEntity.ok().body("Your account has already been verified, you can login !");
        } else if (jwtService.isTokenExpired(token)) {
            return ResponseEntity.ok().body("Token already expired ! <a href=\"" + url + "\">Get a new verification</a>\n");
        } else if (jwtService.isTokenValid(token, userDetails)) {
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
        log.info("Authentication successful for user: {}", response.getAccessToken());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}

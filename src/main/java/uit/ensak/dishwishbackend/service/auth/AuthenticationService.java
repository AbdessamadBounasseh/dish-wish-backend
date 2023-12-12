package uit.ensak.dishwishbackend.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.controller.auth.AuthenticationRequest;
import uit.ensak.dishwishbackend.controller.auth.AuthenticationResponse;
import uit.ensak.dishwishbackend.controller.auth.RegisterRequest;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Role;
import uit.ensak.dishwishbackend.security.JwtService;
import uit.ensak.dishwishbackend.service.ClientService;

import java.io.IOException;

@Service
@Slf4j
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ClientService clientService;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationService emailVerificationService;

    public AuthenticationService(PasswordEncoder passwordEncoder, JwtService jwtService, ClientService clientService, AuthenticationManager authenticationManager, EmailVerificationService emailVerificationService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.clientService = clientService;
        this.authenticationManager = authenticationManager;
        this.emailVerificationService = emailVerificationService;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        Client client = Client
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CLIENT)
                .build();

        clientService.saveClient(client);

        String accessToken = jwtService.generateToken(client);
        String refreshToken = jwtService.generateRefreshToken(client);

        clientService.saveUserVerificationToken(client, accessToken);

        emailVerificationService.sendVerificationEmail(client, accessToken);

        return AuthenticationResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Client client = clientService.getClientByEmail(request.getEmail());
        String accessToken = jwtService.generateToken(client);
        String refreshToken = jwtService.generateRefreshToken(client);

        clientService.revokeAllUserTokens(client);
        clientService.saveUserVerificationToken(client, accessToken);

        return AuthenticationResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Invalid or missing Authorization header for '{}'", request.getRequestURI());
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        log.info("Checking if the user isn't authenticated");

        if (userEmail != null) {
            log.info("Fetching user information");
            var client = clientService.getClientByEmail(userEmail);
            if (jwtService.isTokenValid(refreshToken, client)) {
                var accessToken = jwtService.generateToken(client);
                clientService.revokeAllUserTokens(client);
                clientService.saveUserVerificationToken(client, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                // If just the user isn't enabled, we send an email on refresh
                // Else, we don't need to send anything to the user on refresh
                if (!client.isEnabled()) {
                    emailVerificationService.sendVerificationEmail(client, accessToken);
                    log.info("Refresh email sent to the user");
                }

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
            log.info("Call the filterChain");
        }
    }
}

package uit.ensak.dishwishbackend.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.controller.auth.AuthenticationRequest;
import uit.ensak.dishwishbackend.controller.auth.AuthenticationResponse;
import uit.ensak.dishwishbackend.controller.auth.RegisterRequest;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Role;
import uit.ensak.dishwishbackend.security.JwtUtils;
import uit.ensak.dishwishbackend.service.ClientService;

@Service
@Slf4j
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ClientService clientService;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationService emailVerificationService;

    public AuthenticationService(PasswordEncoder passwordEncoder, JwtUtils jwtUtils, ClientService clientService, AuthenticationManager authenticationManager, EmailVerificationService emailVerificationService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
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

        String jwt = jwtUtils.generateToken(client);

        emailVerificationService.sendVerificationEmail(client, jwt);

        return AuthenticationResponse
                .builder()
                .token(jwt)
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
        String jwt = jwtUtils.generateToken(client);
        return AuthenticationResponse
                .builder()
                .token(jwt)
                .build();
    }
}

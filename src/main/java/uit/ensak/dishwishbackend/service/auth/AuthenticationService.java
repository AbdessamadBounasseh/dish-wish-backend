package uit.ensak.dishwishbackend.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uit.ensak.dishwishbackend.controller.auth.AuthenticationRequest;
import uit.ensak.dishwishbackend.controller.auth.AuthenticationResponse;
import uit.ensak.dishwishbackend.controller.auth.RegisterRequest;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.Role;
import uit.ensak.dishwishbackend.security.JwtService;
import uit.ensak.dishwishbackend.service.ClientService;

import java.util.Collections;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ClientService clientService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(PasswordEncoder passwordEncoder, JwtService jwtService, ClientService clientService, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.clientService = clientService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        Client client = Client
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(Role.CLIENT))
                .build();

        clientService.saveClient(client);

        String jwt = jwtService.generateToken(client);
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
        String jwt = jwtService.generateToken(client);
        return AuthenticationResponse
                .builder()
                .token(jwt)
                .build();
    }
}

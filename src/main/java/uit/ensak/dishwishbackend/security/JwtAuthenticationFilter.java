package uit.ensak.dishwishbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uit.ensak.dishwishbackend.exception.VerificationTokenNotFoundException;
import uit.ensak.dishwishbackend.service.auth.VerificationTokenService;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final VerificationTokenService tokenService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, VerificationTokenService tokenService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Processing authentication for '{}'", request.getRequestURI());

        if (request.getServletPath().contains("/auth")) {
            log.debug("Skipping authentication for '{}'", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Checking if the JWT exist");

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Invalid or missing Authorization header for '{}'", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        log.info("Checking if the user isn't authenticated");

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("Fetching user information");
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid(jwt)) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                log.info("Update the SecurityContextHolder");

                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("User '{}' successfully authenticated", userEmail);
            } else {
                log.warn("Invalid JWT token for '{}'", userEmail);
            }
        }
        log.info("Call the filterChain");
        filterChain.doFilter(request, response);
    }

    private boolean isTokenValid(String jwt) {
        boolean isTokenValid = false;
        try {
            isTokenValid = !tokenService.getByToken(jwt).isExpired()
                    && !tokenService.getByToken(jwt).isRevoked();
        } catch (VerificationTokenNotFoundException e) {
            e.printStackTrace();
        }
        return isTokenValid;
    }
}

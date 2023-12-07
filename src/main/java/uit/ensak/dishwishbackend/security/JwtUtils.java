package uit.ensak.dishwishbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtils {

    private static final String SECRET_KEY = "e0c352481de147d859946af7610bbe1c1626d4e939d828295977491197b67914";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails){
        log.info("Generating token for user '{}'", userDetails.getUsername());
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims,
                                UserDetails userDetails){
        log.info("Generating token for user '{}'", userDetails.getUsername());
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = extractUsername(token);
        boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);

        log.info("Token validation for user '{}': {}", userDetails.getUsername(), isValid ? "valid" : "invalid");

        return isValid;
    }

    public boolean isTokenExpired(String token) {
        boolean isExpired = extractExpiration(token).before(new Date());

        log.debug("Token expiration check: {}", isExpired ? "expired" : "not expired");

        return isExpired;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        log.debug("Generated signing key for JWT");

        return key;
    }
}

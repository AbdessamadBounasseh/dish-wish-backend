package uit.ensak.dishwishbackend.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class PasswordResetToken {

//    private static final int EXPIRATION = 60 * 24;

    private static final long TOKEN_EXPIRATION = 3600000; // one hour

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private String code;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private Date expirationTime;

    public PasswordResetToken(String token, String code, Client client) {
        this.token = token;
        this.code = code;
        this.client = client;
        this.expirationTime = new Date(System.currentTimeMillis() + TOKEN_EXPIRATION);
    }
}
package uit.ensak.dishwishbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expirationTime;

    private static final int EXPIRATION_TIME = 10;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public VerificationToken(Client client, String token) {
        super();
        this.token = token;
        this.client = client;
        this.expirationTime = getTokenExpirationTime();
    }

    public VerificationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = getTokenExpirationTime();
    }

    private Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}

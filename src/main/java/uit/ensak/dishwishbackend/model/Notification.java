package uit.ensak.dishwishbackend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String content;

    @Nullable
    private String seenAt;

    @CreationTimestamp(source = SourceType.DB)
    private Instant createdOn;


    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    private Client client;
}

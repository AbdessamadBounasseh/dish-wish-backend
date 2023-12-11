package uit.ensak.dishwishbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Diet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @CreationTimestamp(source = SourceType.DB)
    private Instant createdOn;


    @ManyToMany
    @JoinTable(
            name = "diet_client",
            joinColumns = { @JoinColumn(name = "diet_id") },
            inverseJoinColumns = { @JoinColumn(name = "client_id") }
    )
    private List<Client> clients;
}

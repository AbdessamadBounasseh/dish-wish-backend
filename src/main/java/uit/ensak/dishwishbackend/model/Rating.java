package uit.ensak.dishwishbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Client client;

    @ManyToOne
    @JsonIgnore
    private Chef chef;

    private double rating;

    @CreationTimestamp(source = SourceType.DB)
    private Instant createdOn;

    @Column(name = "type", insertable = false, updatable = false)
    private String type;
}

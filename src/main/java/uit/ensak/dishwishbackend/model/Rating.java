package uit.ensak.dishwishbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Rating {
//    @EmbeddedId
//    private RatingId id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    @MapsId("clientId")
    private Client client;

    @ManyToOne
    @JoinColumn(name="chef_id", nullable=false)
    @MapsId("chefId")
    private Chef chef;

    private double rating;

//    @ManyToOne
//    @JoinColumn(name="star_id", nullable=false)
//    @MapsId("starId")
//    private Star star;


    @CreationTimestamp(source = SourceType.DB)
    private Instant createdOn;

    @Embeddable
    public static class RatingId implements Serializable {
        private Long clientId;
        private Long chefId;
        private Long starId;

        public RatingId() {
        }
    }
}

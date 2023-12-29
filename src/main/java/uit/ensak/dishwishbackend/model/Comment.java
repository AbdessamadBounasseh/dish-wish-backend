package uit.ensak.dishwishbackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreationTimestamp(source = SourceType.DB)
    private Instant createdOn;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="sender_id", nullable=false)
    private Client sender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="receiver_id", nullable=false)
    private Client receiver;
}

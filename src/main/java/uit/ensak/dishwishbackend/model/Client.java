package uit.ensak.dishwishbackend.model;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.ColumnDefault;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE")
@DiscriminatorValue("CLIENT")
public class Client implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    @ColumnDefault("'src/main/resources/images/profilePhotos/default-profile-pic-dishwish.png'")
    private String photo;

    @CreationTimestamp(source = SourceType.DB)
    private Instant createdOn;

    @UpdateTimestamp(source = SourceType.DB)
    private Instant lastUpdatedOn;


    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Command> commands;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    @ManyToMany(mappedBy = "clients", cascade = CascadeType.ALL)
    private List<Diet> diets;

    @ManyToMany(mappedBy = "clients", cascade = CascadeType.ALL)
    private List<Allergy> allergies;

//    @ElementCollection(targetClass = Role.class)
//    @CollectionTable(name = "client_roles", joinColumns = @JoinColumn(name = "client_id"))
    @Enumerated(EnumType.STRING)
    private Role role;

    // From UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

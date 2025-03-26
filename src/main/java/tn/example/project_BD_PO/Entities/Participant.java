package tn.example.project_BD_PO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "formations")
@Table(name = "participant",
        uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nom is required")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Prenom is required")
    @Column(nullable = false)
    private String prenom;

    @ManyToOne
    @JoinColumn(name = "id_structure",referencedColumnName = "id", nullable = false)
    private Structure structure;

    @ManyToOne
    @JoinColumn(name = "id_profil",referencedColumnName = "id", nullable = false)
    private Profil profil;

    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false)
    private String tel;

    @ManyToMany
    @JoinTable(
            name = "participant_formation",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "formation_id")
    )
    @JsonIgnoreProperties("participants") // Ignores Formation's participants field
    private Set<Formation> formations = new HashSet<>();
}
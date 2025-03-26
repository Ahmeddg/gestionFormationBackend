package tn.example.project_BD_PO.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Table(name = "Formateur")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Formateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String tel; // Changed from int to String

    @Enumerated(EnumType.STRING) // Store enum as a string in DB
    @Column(nullable = false)
    private FormateurType type;

    @ManyToOne
    @JoinColumn(name = "id_employeur", referencedColumnName = "id")
    private Employeur employeur;

    public void setType(String type) {
        this.type = FormateurType.valueOf(type);
    }
}

enum FormateurType {
    INTERNE, EXTERNE
}

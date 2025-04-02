package tn.example.project_BD_PO.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "First name is mandatory")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Last name is mandatory")
    @Column(nullable = false)
    private String prenom;

    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^\\+?[0-9\\s-]{8,20}$",
            message = "Invalid phone number format")
    @Column(nullable = false, unique = true)
    private String tel;

    @NotNull(message = "Formateur type is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormateurType type;

    @ManyToOne
    @JoinColumn(name = "id_employeur", referencedColumnName = "id")
    private Employeur employeur;

    // Custom validation logic for employeur
    @AssertTrue(message = "Employeur is required for EXTERNE formateurs")
    private boolean isEmployeurValid() {
        return type != FormateurType.EXTERNE || employeur != null;
    }

    public void setType(String type) {
        try {
            this.type = FormateurType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid formateur type: " + type +
                    ". Allowed values: INTERNE, EXTERNE");
        }
    }
}

enum FormateurType {
    INTERNE, EXTERNE;
}
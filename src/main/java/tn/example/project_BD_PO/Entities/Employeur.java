package tn.example.project_BD_PO.Entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table
public class Employeur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "nom de l'employeur est obligatoire")
    @Column(nullable = false)
    private String nomEmployeur;
}

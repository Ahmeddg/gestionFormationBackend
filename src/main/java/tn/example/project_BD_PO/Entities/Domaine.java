package tn.example.project_BD_PO.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "Domaine")
public class Domaine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message =  "Libelle est obligatoire")
    @Column(nullable = false , unique = true)
    @NotNull(message = "Libelle ne doit pas être nul")
    private String libelle;


}


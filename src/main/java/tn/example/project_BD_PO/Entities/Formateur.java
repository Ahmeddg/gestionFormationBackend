package tn.example.project_BD_PO.Entities;

import jakarta.persistence.*;
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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private int tel;

    @Column(nullable = false)
    private String type; // interne ou externe

    @ManyToOne
    @JoinColumn(name = "id_employeur", referencedColumnName = "id")
    private Employeur employeur;
}

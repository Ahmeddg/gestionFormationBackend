package tn.example.project_BD_PO.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "Structure")
public class Structure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING) // Store enum as String in DB
    @Column(nullable = false)
    private StructureType libelle;

    public void setLibelle(String libelle) {
        this.libelle = StructureType.valueOf(libelle);
    }
}

enum StructureType {
    CENTRALE, REGIONALE
}

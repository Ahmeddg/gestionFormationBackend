package tn.example.project_BD_PO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "participants")
@Table(name = "formation")
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private int annee;

    @Column(nullable = false)
    private int duree;

    @Column(nullable = false)
    private double budget;

    @Column(nullable = false)
    private String lieu;

    @Column(nullable = false)
    private Date dateDebut;

    @Column
    private Date dateFin;

    @ManyToOne
    @JoinColumn(name = "id_domaine", nullable = false)
    private Domaine domaine;



    @ManyToOne
    @JoinColumn(name = "id_formateur", nullable = false)
    private Formateur formateur;

    @ManyToMany(mappedBy = "formations")
    @JsonIgnoreProperties("formations") // Ignores Participant's formations field
    private Set<Participant> participants = new HashSet<>();

    public void setDateFin() {
        if (this.dateDebut != null) {
            LocalDate startDate = this.dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            this.dateFin = Date.from(startDate.plusDays(this.duree).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }
}
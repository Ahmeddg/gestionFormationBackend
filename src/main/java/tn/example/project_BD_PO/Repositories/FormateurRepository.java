package tn.example.project_BD_PO.Repositories;

import tn.example.project_BD_PO.Entities.Formateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormateurRepository extends JpaRepository<Formateur, Integer> {

}

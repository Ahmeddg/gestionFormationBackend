package tn.example.project_BD_PO.Repositories;

import tn.example.project_BD_PO.Entities.Formation;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface FormationRepository extends JpaRepository<Formation, Integer> {
}

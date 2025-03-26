package tn.example.project_BD_PO.Repositories;

import tn.example.project_BD_PO.Entities.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilRepository extends JpaRepository<Profil, Integer> {
}

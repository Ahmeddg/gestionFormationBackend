package tn.example.project_BD_PO.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.example.project_BD_PO.Entities.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByLogin(String login);
}
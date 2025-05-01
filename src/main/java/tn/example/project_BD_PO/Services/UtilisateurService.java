package tn.example.project_BD_PO.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tn.example.project_BD_PO.Entities.Utilisateur;
import tn.example.project_BD_PO.Repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getUtilisateurById(Integer id) {
        return utilisateurRepository.findById(Long.valueOf(id));
    }
    
    public Optional<Utilisateur> getUtilisateurByUsername(String username) {
        return utilisateurRepository.findByUsername(username);
    }

    public Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }
    
    public void deleteUtilisateur(Integer id) {
        if (!utilisateurRepository.existsById(Long.valueOf(id))) {
            throw new IllegalArgumentException("Utilisateur not found with id: " + id);
        }
        utilisateurRepository.deleteById(Long.valueOf(id));
    }

    public List<String> getAllRoles() {
        List<String> roles = new ArrayList<>();
        utilisateurRepository.findAll().forEach(utilisateur -> roles.add(utilisateur.getRole().name()));
        return roles;
    }

}
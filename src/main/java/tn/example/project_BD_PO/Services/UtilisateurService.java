package tn.example.project_BD_PO.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tn.example.project_BD_PO.Entities.Role;
import tn.example.project_BD_PO.Entities.Utilisateur;
import tn.example.project_BD_PO.Repositories.RoleRepository;
import tn.example.project_BD_PO.Repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private RoleRepository roleRepository;


    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getUtilisateurById(Integer id) {
        return utilisateurRepository.findById(Long.valueOf(id));
    }

    public Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        // Fetch the role by ID from the database
        Role role = roleRepository.findById(utilisateur.getRole().getId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Attach the role to the user
        utilisateur.setRole(role);
        return utilisateurRepository.save(utilisateur);
    }

    public void deleteUtilisateur(Integer id) {
        utilisateurRepository.deleteById(Long.valueOf(id));
    }

    public Utilisateur authenticate(String login, String rawPassword) {
        Utilisateur utilisateur = utilisateurRepository.findByLogin(login).orElse(null);

        if (utilisateur == null) {
            System.out.println("User not found!");
            throw new RuntimeException("User not found!");
        }
        if (utilisateur.matchPassword(rawPassword)){
            return utilisateur;
        }
        return null; // Invalid login or password
    }

}
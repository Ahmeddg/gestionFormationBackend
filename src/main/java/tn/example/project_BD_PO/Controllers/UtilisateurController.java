package tn.example.project_BD_PO.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.example.project_BD_PO.Entities.Role;
import tn.example.project_BD_PO.Entities.Utilisateur;
import tn.example.project_BD_PO.Services.UtilisateurService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @GetMapping("/roles")
    public List<String> getAllRoles() {
        return utilisateurService.getAllRoles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Integer id) {
        return utilisateurService.getUtilisateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/updateRole/{userId}/{role}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<?> updateRole(
            @PathVariable int userId,
            @PathVariable String role
    ) {
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(userId);

        if (utilisateur.isEmpty() || role.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        utilisateur.get().setRole(Role.valueOf(role));
        utilisateurService.saveUtilisateur(utilisateur.get());
        return ResponseEntity.ok(utilisateur);
    }

    @PostMapping
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.saveUtilisateur(utilisateur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(
            @PathVariable Integer id,
            @RequestBody Utilisateur utilisateur
    ) {
        if (utilisateurService.getUtilisateurById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        utilisateur.setId(Long.valueOf(id));
        return ResponseEntity.ok(utilisateurService.saveUtilisateur(utilisateur));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Integer id) {
        if (utilisateurService.getUtilisateurById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.noContent().build();
    }
}
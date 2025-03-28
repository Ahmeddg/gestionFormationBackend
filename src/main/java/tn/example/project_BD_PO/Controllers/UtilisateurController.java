package tn.example.project_BD_PO.Controllers;

import lombok.*;
import org.springframework.http.HttpStatus;
import tn.example.project_BD_PO.Entities.Role;
import tn.example.project_BD_PO.Entities.RoleType;
import tn.example.project_BD_PO.Entities.Utilisateur;
import tn.example.project_BD_PO.Services.RoleService;
import tn.example.project_BD_PO.Services.UtilisateurService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Getter
class LoginRequest {
    private String login;
    private String password;
}


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Integer id) {
        return utilisateurService.getUtilisateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Utilisateur utilisateur = utilisateurService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());

        if (utilisateur == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        return ResponseEntity.ok(utilisateur);
    }

    @PostMapping("/updateRole/{userId}/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable int  userId,@PathVariable int  roleId,@RequestBody LoginRequest loginRequest) {
        Utilisateur adminUtilisateur = utilisateurService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());
        if (adminUtilisateur == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        if (adminUtilisateur.getRole().getNom() != RoleType.ADMINISTRATEUR) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to perform this action");
        }
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(userId);
        Optional<Role> role = roleService.getRoleById(roleId);
        utilisateur.get().setRole(role.get());
        utilisateurService.saveUtilisateur(utilisateur.get());
        return ResponseEntity.ok(utilisateur);
    }

    @PostMapping
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.saveUtilisateur(utilisateur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@PathVariable Integer id, @RequestBody Utilisateur utilisateur) {
        if (utilisateurService.getUtilisateurById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        utilisateur.setId(Long.valueOf(id));
        return ResponseEntity.ok(utilisateurService.saveUtilisateur(utilisateur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Integer id) {
        if (utilisateurService.getUtilisateurById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.noContent().build();
    }
}

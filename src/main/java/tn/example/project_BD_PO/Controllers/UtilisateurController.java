package tn.example.project_BD_PO.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tn.example.project_BD_PO.Entities.Role;
import tn.example.project_BD_PO.Entities.Utilisateur;
import tn.example.project_BD_PO.Services.UtilisateurService;

import java.util.List;
import java.util.Optional;


class PasswordChangeRequest {
    private String username;
    private String oldPassword;
    private String newPassword;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}

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

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PostMapping
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.saveUtilisateur(utilisateur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUtilisateur(
            @PathVariable Integer id,
            @Valid @RequestBody PasswordChangeRequest request,
            @AuthenticationPrincipal UserDetails userDetails,
            BindingResult bindingResult
    ) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // Input validation
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }

        // Verify ownership
        if (!userDetails.getUsername().equals(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Utilisateur> optionalUser = utilisateurService.getUtilisateurById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Utilisateur user = optionalUser.get();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Current password is incorrect");
        }

        // Password strength validation
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            if (request.getNewPassword().length() < 8) {
                return ResponseEntity.badRequest().body("Password must be at least 8 characters");
            }
            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body("New password cannot be same as current");
            }
            user.setPassword(request.getNewPassword());
        }

        try {
            Utilisateur updatedUser = utilisateurService.saveUtilisateur(user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating profile");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Integer id) {
        if (utilisateurService.getUtilisateurById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.noContent().build();
    }
}
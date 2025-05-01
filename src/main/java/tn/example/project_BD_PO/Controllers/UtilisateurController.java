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
import tn.example.project_BD_PO.Security.ErrorResponse;


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
    public ResponseEntity<?> getUtilisateurById(@PathVariable Integer id) {
        return utilisateurService.getUtilisateurById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Utilisateur not found with id: " + id, "UTILISATEUR_NOT_FOUND")));
    }

    @PostMapping("/updateRole/{userId}/{role}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<?> updateRole(
            @PathVariable int userId,
            @PathVariable String role
    ) {
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(userId);
        if (utilisateur.isEmpty() || role.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Utilisateur or role not found", "UTILISATEUR_OR_ROLE_NOT_FOUND"));
        }
        try {
            utilisateur.get().setRole(Role.valueOf(role));
            utilisateurService.saveUtilisateur(utilisateur.get());
            return ResponseEntity.ok(utilisateur);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Invalid role: " + role, "INVALID_ROLE"));
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PostMapping
    public ResponseEntity<?> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        try {
                    if (utilisateur.getPassword() == null || utilisateur.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (utilisateur.getUsername() == null || utilisateur.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (utilisateur.getRole() == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        // Check if the username already exists
        if (utilisateurService.getUtilisateurByUsername(utilisateur.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        utilisateur.setPassword(new BCryptPasswordEncoder().encode(utilisateur.getPassword()));
            return ResponseEntity.ok(utilisateurService.saveUtilisateur(utilisateur));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Error creating utilisateur: " + e.getMessage(), "UTILISATEUR_CREATION_ERROR"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUtilisateur(
            @PathVariable Integer id,
            @Valid @RequestBody PasswordChangeRequest request,
            @AuthenticationPrincipal UserDetails userDetails,
            BindingResult bindingResult
    ) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Invalid request data", "VALIDATION_ERROR"));
        }
        if (!userDetails.getUsername().equals(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.createBasicError(HttpStatus.FORBIDDEN, "You are not allowed to update this user", "FORBIDDEN"));
        }
        Optional<Utilisateur> optionalUser = utilisateurService.getUtilisateurById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Utilisateur not found with id: " + id, "UTILISATEUR_NOT_FOUND"));
        }
        Utilisateur user = optionalUser.get();
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.createBasicError(HttpStatus.UNAUTHORIZED, "Current password is incorrect", "INCORRECT_PASSWORD"));
        }
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            if (request.getNewPassword().length() < 8) {
                return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters", "WEAK_PASSWORD"));
            }
            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "New password cannot be same as current", "PASSWORD_SAME_AS_OLD"));
            }
            user.setPassword(request.getNewPassword());
        }
        try {
            Utilisateur updatedUser = utilisateurService.saveUtilisateur(user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ErrorResponse.createBasicError(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating profile: " + e.getMessage(), "UPDATE_ERROR"));
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUtilisateur(@PathVariable Integer id) {
        if (utilisateurService.getUtilisateurById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Utilisateur not found with id: " + id, "UTILISATEUR_NOT_FOUND"));
        }
        try {
            utilisateurService.deleteUtilisateur(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ErrorResponse.createBasicError(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting utilisateur: " + e.getMessage(), "DELETE_ERROR"));
        }
    }
}
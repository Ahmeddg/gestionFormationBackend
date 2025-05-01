package tn.example.project_BD_PO.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import tn.example.project_BD_PO.Entities.Profil;
import tn.example.project_BD_PO.Services.ProfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.example.project_BD_PO.Security.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/profils")
public class ProfilController {

    @Autowired
    private ProfilService profilService;

    // Get all profils
    @GetMapping
    public List<Profil> getAllProfils() {
        return profilService.getAllProfils();
    }

    // Get profil by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProfilById(@PathVariable Integer id) {
        Optional<Profil> profil = Optional.ofNullable(profilService.getProfil(id));
        return profil
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Profil not found with id: " + id, "PROFIL_NOT_FOUND")));
    }

    // Create a new profil
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PostMapping
    public ResponseEntity<?> createProfil(@RequestBody Profil profil) {
        try {
            return ResponseEntity.ok(profilService.saveProfil(profil));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Error creating profil: " + e.getMessage(), "PROFIL_CREATION_ERROR"));
        }
    }

    // Update an existing profil
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfil(@PathVariable Integer id, @RequestBody Profil profil) {
        if (profilService.getProfil(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Profil not found with id: " + id, "PROFIL_NOT_FOUND"));
        }
        try {
            profil.setId(id);
            return ResponseEntity.ok(profilService.saveProfil(profil));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Error updating profil: " + e.getMessage(), "PROFIL_UPDATE_ERROR"));
        }
    }

    // Delete a profil by ID
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfil(@PathVariable Integer id) {
        Optional<Profil> existingProfil = Optional.ofNullable(profilService.getProfil(id));
        if (existingProfil.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Profil not found with id: " + id, "PROFIL_NOT_FOUND"));
        }
        try {
            profilService.deleteProfil(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ErrorResponse.createBasicError(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting profil: " + e.getMessage(), "PROFIL_DELETE_ERROR"));
        }
    }
}
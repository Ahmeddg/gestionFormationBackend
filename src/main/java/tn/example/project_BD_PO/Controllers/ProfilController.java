package tn.example.project_BD_PO.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import tn.example.project_BD_PO.Entities.Profil;
import tn.example.project_BD_PO.Services.ProfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Profil> getProfilById(@PathVariable Integer id) {
        Optional<Profil> profil = Optional.ofNullable(profilService.getProfil(id));
        return profil.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new profil
    @PostMapping
    public ResponseEntity<Profil> createProfil(@RequestBody Profil profil) {
        return ResponseEntity.ok(profilService.saveProfil(profil));
    }

    // Update an existing profil
    @PutMapping("/{id}")
    public ResponseEntity<Profil> updateProfil(@PathVariable Integer id, @RequestBody Profil profil) {
        if (profilService.getProfil(id) == null) {
            return ResponseEntity.notFound().build();
        }
        profil.setId(id);
        return ResponseEntity.ok(profilService.saveProfil(profil));
    }

    // Delete a profil by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfil(@PathVariable Integer id) {
        Optional<Profil> existingProfil = Optional.ofNullable(profilService.getProfil(id));
        if (existingProfil.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        profilService.deleteProfil(id);
        return ResponseEntity.noContent().build();
    }
}
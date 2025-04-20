package tn.example.project_BD_PO.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import tn.example.project_BD_PO.Entities.Employeur;
import tn.example.project_BD_PO.Services.EmployeurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/employeurs")
public class EmployeurController {

    @Autowired
    private EmployeurService employeurService;

    @GetMapping
    public List<Employeur> getAllEmployeurs() {
        return employeurService.getAllEmployeurs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employeur> getEmployeurById(@PathVariable Integer id) {
        return employeurService.getEmployeurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PostMapping
    public ResponseEntity<Employeur> createEmployeur(@RequestBody Employeur employeur) {
        return ResponseEntity.ok(employeurService.saveEmployeur(employeur));
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PutMapping("/{id}")
    public ResponseEntity<Employeur> updateEmployeur(@PathVariable Integer id, @RequestBody Employeur employeur) {
        if (employeurService.getEmployeurById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employeur.setId(id);
        return ResponseEntity.ok(employeurService.saveEmployeur(employeur));
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployeur(@PathVariable Integer id) {
        if (employeurService.getEmployeurById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            if (employeurService.isEmployeurAssignedToFormateur(id)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Cet employeur est assigné à un formateur et ne peut pas être supprimé."));
            }
            employeurService.deleteEmployeur(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Une erreur inattendue s'est produite lors de la suppression."));
        }
    }

}

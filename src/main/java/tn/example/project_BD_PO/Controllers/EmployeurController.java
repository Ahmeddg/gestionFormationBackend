package tn.example.project_BD_PO.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import tn.example.project_BD_PO.Entities.Employeur;
import tn.example.project_BD_PO.Services.EmployeurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.example.project_BD_PO.Security.ErrorResponse;

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
    public ResponseEntity<?> getEmployeurById(@PathVariable Integer id) {
        return employeurService.getEmployeurById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Employeur not found with id: " + id, "EMPLOYEUR_NOT_FOUND")));
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PostMapping
    public ResponseEntity<?> createEmployeur(@RequestBody Employeur employeur) {
        try {
            return ResponseEntity.ok(employeurService.saveEmployeur(employeur));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Error creating employeur: " + e.getMessage(), "EMPLOYEUR_CREATION_ERROR"));
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployeur(@PathVariable Integer id, @RequestBody Employeur employeur) {
        if (employeurService.getEmployeurById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Employeur not found with id: " + id, "EMPLOYEUR_NOT_FOUND"));
        }
        try {
            employeur.setId(id);
            return ResponseEntity.ok(employeurService.saveEmployeur(employeur));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Error updating employeur: " + e.getMessage(), "EMPLOYEUR_UPDATE_ERROR"));
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployeur(@PathVariable Integer id) {
        if (employeurService.getEmployeurById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Employeur not found with id: " + id, "EMPLOYEUR_NOT_FOUND"));
        }
        try {
            if (employeurService.isEmployeurAssignedToFormateur(id)) {
                return ResponseEntity.badRequest()
                        .body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Cet employeur est assigné à un formateur et ne peut pas être supprimé.", "EMPLOYEUR_ASSIGNED"));
            }
            employeurService.deleteEmployeur(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(ErrorResponse.createBasicError(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur inattendue s'est produite lors de la suppression.", "EMPLOYEUR_DELETE_ERROR"));
        }
    }

}

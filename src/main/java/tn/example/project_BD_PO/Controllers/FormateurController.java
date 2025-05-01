package tn.example.project_BD_PO.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import tn.example.project_BD_PO.Entities.Formateur;
import tn.example.project_BD_PO.Services.FormateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.Map;
import java.util.List;
import org.springframework.http.HttpStatus;
import tn.example.project_BD_PO.Security.ErrorResponse;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/formateurs")
public class FormateurController {

    @Autowired
    private FormateurService formateurService;

    @GetMapping
    public List<Formateur> getAllFormateurs() {
        return formateurService.getAllFormateurs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFormateurById(@PathVariable Integer id) {
        return formateurService.getFormateurById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Formateur not found with id: " + id, "FORMATEUR_NOT_FOUND")));
    }

    @PostMapping
    public ResponseEntity<?> createFormateur(@RequestBody Formateur formateur) {
        try {
            return ResponseEntity.ok(formateurService.saveFormateur(formateur));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Error creating formateur: " + e.getMessage(), "FORMATEUR_CREATION_ERROR"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFormateur(@PathVariable Integer id, @RequestBody Formateur formateur) {
        if (formateurService.getFormateurById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Formateur not found with id: " + id, "FORMATEUR_NOT_FOUND"));
        }
        try {
            formateur.setId(id);
            return ResponseEntity.ok(formateurService.saveFormateur(formateur));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Error updating formateur: " + e.getMessage(), "FORMATEUR_UPDATE_ERROR"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFormateur(@PathVariable Integer id) {
        if (formateurService.getFormateurById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Formateur not found with id: " + id, "FORMATEUR_NOT_FOUND"));
        }
        try {
            formateurService.deleteFormateur(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Ce formateur est assigné à une formation et ne peut pas être supprimé.", "FORMATEUR_ASSIGNED"));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ErrorResponse.createBasicError(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur inattendue s'est produite lors de la suppression.", "FORMATEUR_DELETE_ERROR"));
        }
    }
}

package tn.example.project_BD_PO.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import tn.example.project_BD_PO.Entities.Domaine;
import tn.example.project_BD_PO.Services.DomaineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.example.project_BD_PO.Security.ErrorResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/domaines")
public class DomaineController {

    @Autowired
    private DomaineService domaineService;

    @GetMapping
    public List<Domaine> getAllDomaines() {
        return domaineService.getAllDomaines();
    }

@GetMapping("/{id}")
public ResponseEntity<?> getDomaineById(@PathVariable Integer id) {
    return domaineService.getDomaineById(id)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Domaine not found with id: " + id, "DOMAINE_NOT_FOUND")));
}

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PostMapping
    public ResponseEntity<?> createDomaine(@RequestBody Domaine domaine) {
        try {
            return ResponseEntity.ok(domaineService.saveDomaine(domaine));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Error creating domaine: " + e.getMessage(), "DOMAINE_CREATION_ERROR"));
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDomaine(@PathVariable Integer id, @RequestBody Domaine domaine) {
        if (domaineService.getDomaineById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Domaine not found with id: " + id, "DOMAINE_NOT_FOUND"));
        }
        try {
            domaine.setId(id);
            return ResponseEntity.ok(domaineService.saveDomaine(domaine));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Error updating domaine: " + e.getMessage(), "DOMAINE_UPDATE_ERROR"));
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDomaine(@PathVariable Integer id) {
        if (domaineService.getDomaineById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Domaine not found with id: " + id, "DOMAINE_NOT_FOUND"));
        }
        try {
            domaineService.deleteDomaine(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Ce domaine est assigné à une formation et ne peut pas être supprimé.", "DOMAINE_ASSIGNED"));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ErrorResponse.createBasicError(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur inattendue s'est produite lors de la suppression.", "DOMAINE_DELETE_ERROR"));
        }
    }
}

package tn.example.project_BD_PO.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import tn.example.project_BD_PO.Entities.Domaine;
import tn.example.project_BD_PO.Services.DomaineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Domaine> getDomaineById(@PathVariable Integer id) {
        return domaineService.getDomaineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PostMapping
    public ResponseEntity<Domaine> createDomaine(@RequestBody Domaine domaine) {
        return ResponseEntity.ok(domaineService.saveDomaine(domaine));
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PutMapping("/{id}")
    public ResponseEntity<Domaine> updateDomaine(@PathVariable Integer id, @RequestBody Domaine domaine) {
        if (domaineService.getDomaineById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        domaine.setId(id);
        return ResponseEntity.ok(domaineService.saveDomaine(domaine));
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDomaine(@PathVariable Integer id) {
        if (domaineService.getDomaineById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            domaineService.deleteDomaine(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Ce domaine est assigné à une formation et ne peut pas être supprimé."));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Une erreur inattendue s'est produite lors de la suppression."));
        }
    }
}

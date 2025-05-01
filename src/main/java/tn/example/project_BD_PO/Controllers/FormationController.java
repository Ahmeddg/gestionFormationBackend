package tn.example.project_BD_PO.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import tn.example.project_BD_PO.Entities.Formation;
import tn.example.project_BD_PO.Services.FormationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.example.project_BD_PO.Security.ErrorResponse;
import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/formations")
public class FormationController {

    private final FormationService formationService;

    public FormationController(FormationService formationService) {
        this.formationService = formationService;
    }

    @GetMapping
    public List<Formation> getAllFormations() {
        return formationService.getAllFormations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFormationById(@PathVariable int id) {
        return formationService.getFormationById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Formation not found with id: " + id, "FORMATION_NOT_FOUND")));
    }

    @PostMapping
    public ResponseEntity<?> createFormation(@RequestBody Formation formation) {
        try {
            return ResponseEntity.ok(formationService.saveFormation(formation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Error creating formation: " + e.getMessage(), "FORMATION_CREATION_ERROR"));
        }
    }

    @PostMapping("/{formationId}/add_participants")
    public ResponseEntity<?> addParticipantToFormation(@RequestBody List<Integer> participantIds, @PathVariable int formationId) {
        try {
            Formation formation = formationService.getFormationById(formationId)
                    .orElseThrow(() -> new RuntimeException("Formation not found"));
            formationService.addParticipantsToFormation(formation, participantIds);
            return ResponseEntity.ok(formationService.saveFormation(formation));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, ex.getMessage(), "ADD_PARTICIPANT_ERROR"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFormation(@PathVariable int id, @RequestBody Formation formation) {
        if (formationService.getFormationById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Formation not found with id: " + id, "FORMATION_NOT_FOUND"));
        }
        try {
            formation.setId(id);
            return ResponseEntity.ok(formationService.saveFormation(formation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, "Error updating formation: " + e.getMessage(), "FORMATION_UPDATE_ERROR"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFormation(@PathVariable int id) {
        if (formationService.getFormationById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, "Formation not found with id: " + id, "FORMATION_NOT_FOUND"));
        }
        try {
            formationService.deleteFormation(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ErrorResponse.createBasicError(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting formation: " + e.getMessage(), "FORMATION_DELETE_ERROR"));
        }
    }
}

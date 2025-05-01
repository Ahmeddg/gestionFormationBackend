package tn.example.project_BD_PO.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import tn.example.project_BD_PO.Entities.Participant;
import tn.example.project_BD_PO.Services.ParticipantService;
import tn.example.project_BD_PO.Security.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping
    public ResponseEntity<List<Participant>> getAllParticipants() {
        return ResponseEntity.ok(participantService.getAllParticipants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getParticipantById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(participantService.getParticipantById(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, ex.getMessage(), "PARTICIPANT_NOT_FOUND"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createParticipant(@RequestBody Participant participant) {
        try {
            Participant savedParticipant = participantService.saveParticipant(participant);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedParticipant.getId())
                    .toUri();
            return ResponseEntity.created(location).body(savedParticipant);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, ex.getMessage(), "PARTICIPANT_CREATION_ERROR"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateParticipant(
            @PathVariable Integer id,
            @RequestBody Participant participant) {
        try {
            return ResponseEntity.ok(participantService.updateParticipant(id, participant));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, ex.getMessage(), "PARTICIPANT_UPDATE_ERROR"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParticipant(@PathVariable Integer id) {
        try {
            participantService.deleteParticipant(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.createBasicError(HttpStatus.NOT_FOUND, ex.getMessage(), "PARTICIPANT_DELETE_ERROR"));
        }
    }
    @PutMapping("/{participantId}/formations/{formationId}")
    public ResponseEntity<?> addFormationToParticipant(
            @PathVariable Integer participantId,
            @PathVariable Integer formationId) {
        try {
            Participant updatedParticipant = participantService.addFormationToParticipant(participantId, formationId);
            return ResponseEntity.ok(updatedParticipant);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, ex.getMessage(), "ADD_FORMATION_ERROR"));
        }
    }

    // Endpoint to remove formation from participant
    @DeleteMapping("/{participantId}/formations/{formationId}")
    public ResponseEntity<?> removeFormationFromParticipant(
            @PathVariable Integer participantId,
            @PathVariable Integer formationId) {
        try {
            Participant updatedParticipant = participantService.removeFormationFromParticipant(participantId, formationId);
            return ResponseEntity.ok(updatedParticipant);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, ex.getMessage(), "REMOVE_FORMATION_ERROR"));
        }
    }

    // Exception handling
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.createBasicError(HttpStatus.BAD_REQUEST, ex.getMessage(), "RUNTIME_ERROR"));
    }
}
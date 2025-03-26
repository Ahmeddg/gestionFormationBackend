package tn.example.project_BD_PO.Controllers;

import tn.example.project_BD_PO.Entities.Participant;
import tn.example.project_BD_PO.Services.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping
    public ResponseEntity<List<Participant>> getAllParticipants() {
        return ResponseEntity.ok(participantService.getAllParticipants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Integer id) {
        return ResponseEntity.ok(participantService.getParticipantById(id));
    }

    @PostMapping
    public ResponseEntity<Participant> createParticipant(@RequestBody Participant participant) {
        Participant savedParticipant = participantService.saveParticipant(participant);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedParticipant.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedParticipant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(
            @PathVariable Integer id,
            @RequestBody Participant participant) {
        return ResponseEntity.ok(participantService.updateParticipant(id, participant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Integer id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{participantId}/formations/{formationId}")
    public ResponseEntity<Participant> addFormationToParticipant(
            @PathVariable Integer participantId,
            @PathVariable Integer formationId) {
        try {
            Participant updatedParticipant = participantService.addFormationToParticipant(participantId, formationId);
            return ResponseEntity.ok(updatedParticipant);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);  // Or customize your error response
        }
    }

    // Endpoint to remove formation from participant
    @DeleteMapping("/{participantId}/formations/{formationId}")
    public ResponseEntity<Participant> removeFormationFromParticipant(
            @PathVariable Integer participantId,
            @PathVariable Integer formationId) {
        try {
            Participant updatedParticipant = participantService.removeFormationFromParticipant(participantId, formationId);
            return ResponseEntity.ok(updatedParticipant);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);  // Or customize your error response
        }
    }

    // Exception handling
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
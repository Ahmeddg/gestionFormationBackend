package tn.example.project_BD_PO.Controllers;

import tn.example.project_BD_PO.Entities.Formation;
import tn.example.project_BD_PO.Services.FormationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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
    public ResponseEntity<Formation> getFormationById(@PathVariable int id) {
        return formationService.getFormationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Formation> createFormation(@RequestBody Formation formation) {
        return ResponseEntity.ok(formationService.saveFormation(formation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formation> updateFormation(@PathVariable int id, @RequestBody Formation formation) {
        if (formationService.getFormationById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        formation.setId(id);
        return ResponseEntity.ok(formationService.saveFormation(formation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormation(@PathVariable int id) {
        if (formationService.getFormationById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        formationService.deleteFormation(id);
        return ResponseEntity.noContent().build();
    }
}

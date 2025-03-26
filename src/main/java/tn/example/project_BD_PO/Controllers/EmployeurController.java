package tn.example.project_BD_PO.Controllers;

import tn.example.project_BD_PO.Entities.Employeur;
import tn.example.project_BD_PO.Services.EmployeurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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

    @PostMapping
    public ResponseEntity<Employeur> createEmployeur(@RequestBody Employeur employeur) {
        return ResponseEntity.ok(employeurService.saveEmployeur(employeur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employeur> updateEmployeur(@PathVariable Integer id, @RequestBody Employeur employeur) {
        if (employeurService.getEmployeurById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employeur.setId(id);
        return ResponseEntity.ok(employeurService.saveEmployeur(employeur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeur(@PathVariable Integer id) {
        if (employeurService.getEmployeurById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employeurService.deleteEmployeur(id);
        return ResponseEntity.noContent().build();
    }
}

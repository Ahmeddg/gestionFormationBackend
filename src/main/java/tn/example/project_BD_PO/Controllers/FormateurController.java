package tn.example.project_BD_PO.Controllers;

import tn.example.project_BD_PO.Entities.Formateur;
import tn.example.project_BD_PO.Services.FormateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/formateurs")
public class FormateurController {

    @Autowired
    private FormateurService formateurService;

    @GetMapping
    public List<Formateur> getAllFormateurs() {
        return formateurService.getAllFormateurs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formateur> getFormateurById(@PathVariable Integer id) {
        return formateurService.getFormateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Formateur> createFormateur(@RequestBody Formateur formateur) {
        return ResponseEntity.ok(formateurService.saveFormateur(formateur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formateur> updateFormateur(@PathVariable Integer id, @RequestBody Formateur formateur) {
        if (formateurService.getFormateurById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        formateur.setId(id);
        return ResponseEntity.ok(formateurService.saveFormateur(formateur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormateur(@PathVariable Integer id) {
        if (formateurService.getFormateurById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        formateurService.deleteFormateur(id);
        return ResponseEntity.noContent().build();
    }
}

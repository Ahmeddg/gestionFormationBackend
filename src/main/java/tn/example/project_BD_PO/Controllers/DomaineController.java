package tn.example.project_BD_PO.Controllers;

import tn.example.project_BD_PO.Entities.Domaine;
import tn.example.project_BD_PO.Services.DomaineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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

    @PostMapping
    public ResponseEntity<Domaine> createDomaine(@RequestBody Domaine domaine) {
        return ResponseEntity.ok(domaineService.saveDomaine(domaine));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Domaine> updateDomaine(@PathVariable Integer id, @RequestBody Domaine domaine) {
        if (domaineService.getDomaineById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        domaine.setId(id);
        return ResponseEntity.ok(domaineService.saveDomaine(domaine));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomaine(@PathVariable Integer id) {
        if (domaineService.getDomaineById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        domaineService.deleteDomaine(id);
        return ResponseEntity.noContent().build();
    }
}

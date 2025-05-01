package tn.example.project_BD_PO.Services;

import tn.example.project_BD_PO.Entities.Employeur;
import tn.example.project_BD_PO.Repositories.EmployeurRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import tn.example.project_BD_PO.Repositories.FormateurRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeurService {

    @Autowired
    private EmployeurRepository employeurRepository;

    // Retrieve all employeurs
    public List<Employeur> getAllEmployeurs() {
        return employeurRepository.findAll();
    }

    // Retrieve employeur by id
    public Optional<Employeur> getEmployeurById(Integer id) {
        return employeurRepository.findById(id);
    }

    // Save new or update existing employeur
    public Employeur saveEmployeur(Employeur employeur) {
        if (employeur.getNomEmployeur() == null || employeur.getNomEmployeur().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'employeur est obligatoire et ne doit pas être vide.");
        }
        // Uniqueness check for create
        if (employeur.getId() == null && employeurRepository.findAll().stream().anyMatch(e -> e.getNomEmployeur().equalsIgnoreCase(employeur.getNomEmployeur()))) {
            throw new IllegalArgumentException("Un employeur avec ce nom existe déjà.");
        }
        // Uniqueness check for update
        if (employeur.getId() != null) {
            Optional<Employeur> existing = employeurRepository.findById(employeur.getId());
            if (existing.isPresent() && !existing.get().getNomEmployeur().equalsIgnoreCase(employeur.getNomEmployeur()) && employeurRepository.findAll().stream().anyMatch(e -> e.getNomEmployeur().equalsIgnoreCase(employeur.getNomEmployeur()))) {
                throw new IllegalArgumentException("Un employeur avec ce nom existe déjà.");
            }
        }
        return employeurRepository.save(employeur);
    }

    @Autowired
    private FormateurRepository formateurRepository;

    public boolean isEmployeurAssignedToFormateur(Integer id) {
        return formateurRepository.existsByEmployeurId(id);
    }

    // Delete employeur by id
    public void deleteEmployeur(Integer id) {
        if (!employeurRepository.existsById(id)) {
            throw new IllegalArgumentException("Employeur introuvable avec l'id: " + id);
        }
        employeurRepository.deleteById(id);
    }
}

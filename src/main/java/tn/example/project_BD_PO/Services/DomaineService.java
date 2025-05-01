package tn.example.project_BD_PO.Services;

import tn.example.project_BD_PO.Entities.Domaine;
import tn.example.project_BD_PO.Repositories.DomaineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DomaineService {

    @Autowired
    private DomaineRepository domaineRepository;

    // Retrieve all domaines
    public List<Domaine> getAllDomaines() {
        return domaineRepository.findAll();
    }

    // Retrieve domaine by id
    public Optional<Domaine> getDomaineById(Integer id) {
        return domaineRepository.findById(id);
    }

    // Save new or update existing domaine
    public Domaine saveDomaine(Domaine domaine) {
        if (domaine.getLibelle() == null || domaine.getLibelle().trim().isEmpty()) {
            throw new IllegalArgumentException("Libelle est obligatoire et ne doit pas être vide.");
        }
        if (domaine.getLibelle().length() < 2) {
            throw new IllegalArgumentException("Libelle doit contenir au moins 2 caractères.");
        }
        // Uniqueness check for create
        if (domaine.getId() == null && domaineRepository.findAll().stream().anyMatch(d -> d.getLibelle().equalsIgnoreCase(domaine.getLibelle()))) {
            throw new IllegalArgumentException("Un domaine avec ce libelle existe déjà.");
        }
        // Uniqueness check for update
        if (domaine.getId() != null) {
            Optional<Domaine> existing = domaineRepository.findById(domaine.getId());
            if (existing.isPresent() && !existing.get().getLibelle().equalsIgnoreCase(domaine.getLibelle()) && domaineRepository.findAll().stream().anyMatch(d -> d.getLibelle().equalsIgnoreCase(domaine.getLibelle()))) {
                throw new IllegalArgumentException("Un domaine avec ce libelle existe déjà.");
            }
        }
        return domaineRepository.save(domaine);
    }

    // Delete domaine by id
    public void deleteDomaine(Integer id) {
        if (!domaineRepository.existsById(id)) {
            throw new IllegalArgumentException("Domaine introuvable avec l'id: " + id);
        }
        domaineRepository.deleteById(id);
    }
}

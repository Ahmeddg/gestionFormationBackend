package tn.example.project_BD_PO.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.example.project_BD_PO.Entities.Profil;
import tn.example.project_BD_PO.Repositories.ProfilRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProfilService {
    @Autowired
    private ProfilRepository profilRepository;
    public Profil getProfil(int id) {
        Optional<Profil> profil = profilRepository.findById(id);
        return profil.orElse(null);
    }
    public List<Profil> getAllProfils() {
        return profilRepository.findAll();
    }

    public Profil saveProfil(Profil profil) {
        if (profil.getLibelle() == null || profil.getLibelle().trim().isEmpty()) {
            throw new IllegalArgumentException("Libelle est obligatoire et ne doit pas être vide.");
        }
        // Uniqueness check for create
        if (profil.getId() == null && profilRepository.findAll().stream().anyMatch(p -> p.getLibelle().equalsIgnoreCase(profil.getLibelle()))) {
            throw new IllegalArgumentException("Un profil avec ce libelle existe déjà.");
        }
        // Uniqueness check for update
        if (profil.getId() != null) {
            Optional<Profil> existing = profilRepository.findById(profil.getId());
            if (existing.isPresent() && !existing.get().getLibelle().equalsIgnoreCase(profil.getLibelle()) && profilRepository.findAll().stream().anyMatch(p -> p.getLibelle().equalsIgnoreCase(profil.getLibelle()))) {
                throw new IllegalArgumentException("Un profil avec ce libelle existe déjà.");
            }
        }
        return profilRepository.save(profil);
    }

    public void deleteProfil(Integer id) {
        if (!profilRepository.existsById(id)) {
            throw new IllegalArgumentException("Profil introuvable avec l'id: " + id);
        }
        profilRepository.deleteById(id);
    }
    public Profil updateProfil(Profil profil) {
        return profilRepository.save(profil);
    }

}

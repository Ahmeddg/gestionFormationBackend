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
        return profilRepository.save(profil);
    }
    public void deleteProfil(int id) {
        profilRepository.deleteById(id);
    }
    public Profil updateProfil(Profil profil) {
        return profilRepository.save(profil);
    }

}

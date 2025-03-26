package tn.example.project_BD_PO.Services;

import tn.example.project_BD_PO.Entities.Formateur;
import tn.example.project_BD_PO.Repositories.FormateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FormateurService {

    @Autowired
    private FormateurRepository formateurRepository;

    // Retrieve all formateurs
    public List<Formateur> getAllFormateurs() {
        return formateurRepository.findAll();
    }

    // Retrieve formateur by id
    public Optional<Formateur> getFormateurById(Integer id) {
        return formateurRepository.findById(id);
    }

    // Save new or update existing formateur
    public Formateur saveFormateur(Formateur formateur) {
        return formateurRepository.save(formateur);
    }

    // Delete formateur by id
    public void deleteFormateur(Integer id) {
        formateurRepository.deleteById(id);
    }
}

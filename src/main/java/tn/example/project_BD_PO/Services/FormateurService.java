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
        if (formateur.getNom() == null || formateur.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Nom est obligatoire et ne doit pas être vide.");
        }
        if (formateur.getPrenom() == null || formateur.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Prenom est obligatoire et ne doit pas être vide.");
        }
        if (formateur.getEmail() == null || formateur.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email est obligatoire et ne doit pas être vide.");
        }
        if (!formateur.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Format d'email invalide.");
        }
        if (formateur.getTel() == null || formateur.getTel().trim().isEmpty()) {
            throw new IllegalArgumentException("Numéro de téléphone est obligatoire et ne doit pas être vide.");
        }
        if (formateur.getType() == null) {
            throw new IllegalArgumentException("Type de formateur est obligatoire.");
        }
        if (formateur.getType().name().equals("EXTERNE") && formateur.getEmployeur() == null) {
            throw new IllegalArgumentException("Employeur est obligatoire pour les formateurs EXTERNE.");
        }
        // Uniqueness check for email (create)
        if (formateur.getId() == null && formateurRepository.findAll().stream().anyMatch(f -> f.getEmail().equalsIgnoreCase(formateur.getEmail()))) {
            throw new IllegalArgumentException("Un formateur avec cet email existe déjà.");
        }
        // Uniqueness check for tel (create)
        if (formateur.getId() == null && formateurRepository.findAll().stream().anyMatch(f -> f.getTel().equals(formateur.getTel()))) {
            throw new IllegalArgumentException("Un formateur avec ce numéro de téléphone existe déjà.");
        }
        // Uniqueness check for update
        if (formateur.getId() != null) {
            Optional<Formateur> existing = formateurRepository.findById(formateur.getId());
            if (existing.isPresent()) {
                if (!existing.get().getEmail().equalsIgnoreCase(formateur.getEmail()) && formateurRepository.findAll().stream().anyMatch(f -> f.getEmail().equalsIgnoreCase(formateur.getEmail()))) {
                    throw new IllegalArgumentException("Un formateur avec cet email existe déjà.");
                }
                if (!existing.get().getTel().equals(formateur.getTel()) && formateurRepository.findAll().stream().anyMatch(f -> f.getTel().equals(formateur.getTel()))) {
                    throw new IllegalArgumentException("Un formateur avec ce numéro de téléphone existe déjà.");
                }
            }
        }
        return formateurRepository.save(formateur);
    }

    // Delete formateur by id
    public void deleteFormateur(Integer id) {
        if (!formateurRepository.existsById(id)) {
            throw new IllegalArgumentException("Formateur introuvable avec l'id: " + id);
        }
        formateurRepository.deleteById(id);
    }
}

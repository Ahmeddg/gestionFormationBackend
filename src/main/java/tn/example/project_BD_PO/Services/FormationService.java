package tn.example.project_BD_PO.Services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import tn.example.project_BD_PO.Entities.Domaine;
import tn.example.project_BD_PO.Entities.Formateur;
import tn.example.project_BD_PO.Entities.Formation;
import tn.example.project_BD_PO.Entities.Participant;
import tn.example.project_BD_PO.Repositories.DomaineRepository;
import tn.example.project_BD_PO.Repositories.FormateurRepository;
import tn.example.project_BD_PO.Repositories.FormationRepository;
import org.springframework.stereotype.Service;
import tn.example.project_BD_PO.Repositories.ParticipantRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FormationService {
    
    private final FormationRepository formationRepository;
    private final FormateurRepository formateurRepository;
    private final DomaineRepository domaineRepository;
    private final ParticipantRepository participantRepository;


    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

    public Optional<Formation> getFormationById(int id) {
        return formationRepository.findById(id);
    }

    public Formation saveFormation(Formation formation) {
        if (formation.getTitre() == null || formation.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Titre est obligatoire et ne doit pas être vide.");
        }
        if (formation.getAnnee() <= 0) {
            throw new IllegalArgumentException("Année doit être supérieure à 0.");
        }
        if (formation.getDuree() <= 0) {
            throw new IllegalArgumentException("Durée doit être supérieure à 0.");
        }
        if (formation.getBudget() < 0) {
            throw new IllegalArgumentException("Le budget ne peut pas être négatif.");
        }
        if (formation.getLieu() == null || formation.getLieu().trim().isEmpty()) {
            throw new IllegalArgumentException("Lieu est obligatoire et ne doit pas être vide.");
        }
        if (formation.getDomaine() == null) {
            throw new IllegalArgumentException("Domaine est obligatoire.");
        }
        if (formation.getFormateur() == null) {
            throw new IllegalArgumentException("Formateur est obligatoire.");
        }
        Formateur formateur = formateurRepository.findById(formation.getFormateur().getId()).orElseThrow();
        Domaine domaine = domaineRepository.findById(formation.getDomaine().getId()).orElseThrow();
        formation.setDomaine(domaine);
        formation.setFormateur(formateur);
        formation.setDateFin();
        return formationRepository.save(formation);
    }

    public void addParticipantsToFormation(Formation formation, List<Integer> participantIds) {
        for (Integer id : participantIds) {
            Participant participant = participantRepository.findById(id).orElseThrow();
            participant.getFormations().add(formation); // Owning side
            participantRepository.save(participant); // Save owning side
        }
    }

    @Transactional
    public void deleteFormation(int formationId) {
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new EntityNotFoundException("Formation not found"));

        // Clear participants from the formation
        for (Participant participant : new ArrayList<>(formation.getParticipants())) {
            participant.getFormations().remove(formation);
            participantRepository.save(participant);
        }

        formationRepository.delete(formation);
    }

    public void deleteFormation(Integer id) {
        if (!formationRepository.existsById(id)) {
            throw new IllegalArgumentException("Formation introuvable avec l'id: " + id);
        }
        formationRepository.deleteById(id);
    }
}

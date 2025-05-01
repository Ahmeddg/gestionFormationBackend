package tn.example.project_BD_PO.Services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.example.project_BD_PO.Entities.*;
import tn.example.project_BD_PO.Repositories.*;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final ProfilRepository profilRepository;
    private final FormationRepository formationRepository;


    public Participant saveParticipant(Participant participant) {
        if (participant.getNom() == null || participant.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Nom est obligatoire et ne doit pas être vide.");
        }
        if (participant.getPrenom() == null || participant.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Prenom est obligatoire et ne doit pas être vide.");
        }
        if (participant.getEmail() == null || participant.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email est obligatoire et ne doit pas être vide.");
        }
        if (!participant.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Format d'email invalide.");
        }
        if (participant.getTel() == null || participant.getTel().trim().isEmpty()) {
            throw new IllegalArgumentException("Numéro de téléphone est obligatoire et ne doit pas être vide.");
        }
        // Uniqueness check for email (create)
        if (participant.getId() == null && participantRepository.findAll().stream().anyMatch(p -> p.getEmail().equalsIgnoreCase(participant.getEmail()))) {
            throw new IllegalArgumentException("Un participant avec cet email existe déjà.");
        }
        // Uniqueness check for tel (create)
        if (participant.getId() == null && participantRepository.findAll().stream().anyMatch(p -> p.getTel().equals(participant.getTel()))) {
            throw new IllegalArgumentException("Un participant avec ce numéro de téléphone existe déjà.");
        }
        // Uniqueness check for update
        if (participant.getId() != null) {
            Optional<Participant> existing = participantRepository.findById(participant.getId());
            if (existing.isPresent()) {
                if (!existing.get().getEmail().equalsIgnoreCase(participant.getEmail()) && participantRepository.findAll().stream().anyMatch(p -> p.getEmail().equalsIgnoreCase(participant.getEmail()))) {
                    throw new IllegalArgumentException("Un participant avec cet email existe déjà.");
                }
                if (!existing.get().getTel().equals(participant.getTel()) && participantRepository.findAll().stream().anyMatch(p -> p.getTel().equals(participant.getTel()))) {
                    throw new IllegalArgumentException("Un participant avec ce numéro de téléphone existe déjà.");
                }
            }
        }

        Profil profil = profilRepository.findById(participant.getProfil().getId())
                .orElseThrow(() -> new RuntimeException("Profil not found"));

        participant.setProfil(profil);
        return participantRepository.save(participant);
    }

    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }


    public Participant getParticipantById(Integer id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
    }

    public Participant updateParticipant(Integer id, Participant updatedParticipant) {
        Participant existing = getParticipantById(id);

        existing.setNom(updatedParticipant.getNom());
        existing.setPrenom(updatedParticipant.getPrenom());
        existing.setTel(updatedParticipant.getTel());

        if (!updatedParticipant.getEmail().equals(existing.getEmail())) {
            if (participantRepository.existsByEmail(updatedParticipant.getEmail())) {
                throw new RuntimeException("New email already exists");
            }
            existing.setEmail(updatedParticipant.getEmail());
        }

        if (!updatedParticipant.getProfil().getId().equals(existing.getProfil().getId())) {
            Profil newProfil = profilRepository.findById(updatedParticipant.getProfil().getId())
                    .orElseThrow(() -> new RuntimeException("New profil not found"));
            existing.setProfil(newProfil);
        }

        return participantRepository.save(existing);
    }


    public void deleteParticipant(Integer id) {
        if (!participantRepository.existsById(id)) {
            throw new IllegalArgumentException("Participant introuvable avec l'id: " + id);
        }
        Participant participant = getParticipantById(id);
        // Remove from all formations
        participant.getFormations().forEach(formation ->
                formation.getParticipants().remove(participant));
        participantRepository.delete(participant);
    }


    public Participant addFormationToParticipant(Integer participantId, Integer formationId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException("Participant not found"));

        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new EntityNotFoundException("Formation not found"));

        // Only modify owning side (participant)
        participant.getFormations().add(formation);
        formation.getParticipants().add(participant);

        return participantRepository.save(participant);
    }


    public Participant removeFormationFromParticipant(Integer participantId, Integer formationId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException("Participant not found"));

        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new EntityNotFoundException("Formation not found"));

        // Only modify owning side (participant)
        participant.getFormations().remove(formation);
        formation.getParticipants().remove(participant);

        return participantRepository.save(participant);
    }
}
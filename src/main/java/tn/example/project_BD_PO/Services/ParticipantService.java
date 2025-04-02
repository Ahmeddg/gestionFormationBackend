package tn.example.project_BD_PO.Services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.example.project_BD_PO.Entities.*;
import tn.example.project_BD_PO.Repositories.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final ProfilRepository profilRepository;
    private final FormationRepository formationRepository;


    public Participant saveParticipant(Participant participant) {

        Profil profil = profilRepository.findById(participant.getProfil().getId())
                .orElseThrow(() -> new RuntimeException("Profil not found"));

        if (participantRepository.existsByEmail(participant.getEmail())) {
            throw new RuntimeException("Email already exists");
        }


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
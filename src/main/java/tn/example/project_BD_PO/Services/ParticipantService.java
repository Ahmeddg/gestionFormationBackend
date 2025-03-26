package tn.example.project_BD_PO.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.example.project_BD_PO.Entities.*;
import tn.example.project_BD_PO.Repositories.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final StructureRepository structureRepository;
    private final ProfilRepository profilRepository;
    private final FormationRepository formationRepository;

    // Create participant with validation
    @Transactional
    public Participant saveParticipant(Participant participant) {
        Structure structure = structureRepository.findById(participant.getStructure().getId())
                .orElseThrow(() -> new RuntimeException("Structure not found"));

        Profil profil = profilRepository.findById(participant.getProfil().getId())
                .orElseThrow(() -> new RuntimeException("Profil not found"));

        if (participantRepository.existsByEmail(participant.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        participant.setStructure(structure);
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

    @Transactional
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
        // Update relationships if changed
        if (!updatedParticipant.getStructure().getId().equals(existing.getStructure().getId())) {
            Structure newStructure = structureRepository.findById(updatedParticipant.getStructure().getId())
                    .orElseThrow(() -> new RuntimeException("New structure not found"));
            existing.setStructure(newStructure);
        }

        if (!updatedParticipant.getProfil().getId().equals(existing.getProfil().getId())) {
            Profil newProfil = profilRepository.findById(updatedParticipant.getProfil().getId())
                    .orElseThrow(() -> new RuntimeException("New profil not found"));
            existing.setProfil(newProfil);
        }

        return participantRepository.save(existing);
    }

    @Transactional
    public void deleteParticipant(Integer id) {
        Participant participant = getParticipantById(id);
        // Remove from all formations
        participant.getFormations().forEach(formation ->
                formation.getParticipants().remove(participant));
        participantRepository.delete(participant);
    }

    @Transactional
    public Participant addFormationToParticipant(Integer participantId, int formationId) {
        Participant participant = getParticipantById(participantId);
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found"));

        participant.getFormations().add(formation);
        formation.getParticipants().add(participant);

        return participantRepository.save(participant);
    }

    @Transactional
    public Participant removeFormationFromParticipant(Integer participantId, int formationId) {
        Participant participant = getParticipantById(participantId);
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found"));

        if (participant.getFormations().contains(formation)) {
            participant.getFormations().remove(formation);
            formation.getParticipants().remove(participant);
        }

        return participantRepository.save(participant);
    }
}
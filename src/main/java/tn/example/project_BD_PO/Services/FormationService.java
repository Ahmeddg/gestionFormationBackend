package tn.example.project_BD_PO.Services;

import tn.example.project_BD_PO.Entities.Domaine;
import tn.example.project_BD_PO.Entities.Formateur;
import tn.example.project_BD_PO.Entities.Formation;
import tn.example.project_BD_PO.Entities.Participant;
import tn.example.project_BD_PO.Repositories.DomaineRepository;
import tn.example.project_BD_PO.Repositories.FormateurRepository;
import tn.example.project_BD_PO.Repositories.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.example.project_BD_PO.Repositories.ParticipantRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FormationService {
    @Autowired
    private FormationRepository formationRepository;
    @Autowired
    private FormateurRepository formateurRepository;
    @Autowired
    private DomaineRepository domaineRepository;
    @Autowired
    private ParticipantRepository participantRepository;

    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

    public Optional<Formation> getFormationById(int id) {
        return formationRepository.findById(id);
    }

    public Formation saveFormation(Formation formation) {
        Formateur formateur = formateurRepository.findById(formation.getDomaine().getId()).orElseThrow();
        Domaine domaine = domaineRepository.findById(formation.getFormateur().getId()).orElseThrow();
        formation.setDomaine(domaine);
        formation.setFormateur(formateur);
        if (!formation.getParticipants().isEmpty()){
            Set<Participant> participants = new HashSet<>(
                    participantRepository.findAllById(
                            formation.getParticipants().stream()
                                    .map(Participant::getId)
                                    .toList()
                    )
            );

            if (participants.isEmpty()) {
                throw new RuntimeException("Participants not found");
            }
            formation.setParticipants(participants);
        }
        return formationRepository.save(formation);
    }

    public void deleteFormation(int id) {
        formationRepository.deleteById(id);
    }
}

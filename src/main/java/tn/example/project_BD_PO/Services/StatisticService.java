package tn.example.project_BD_PO.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.example.project_BD_PO.Entities.*;
import tn.example.project_BD_PO.Repositories.FormateurRepository;
import tn.example.project_BD_PO.Repositories.FormationRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final FormationRepository formationRepository;
    private final FormateurRepository formateurRepository;

    public List<Formation> getBestFormations() {
        return formationRepository.findMostPopularFormations();
    }

    public long getTotalFormationsBudget() {
        return formationRepository.sumAllBudgets() != null ?
                formationRepository.sumAllBudgets() : 0L;
    }

    public Map<Integer, Long> getFormationsBudgetPerYear() {
        return formationRepository.sumBudgetByYear().stream()
                .collect(Collectors.toMap(
                        FormationRepository.BudgetByYear::getYear,
                        FormationRepository.BudgetByYear::getTotalBudget
                ));
    }
    
    public Map<Formateur, Integer> getBestFormateur() {
        return formateurRepository.findAll().stream()
                .collect(Collectors.toMap(
                        formateur -> formateur,
                        formateur -> (int) formationRepository.countByFormateur(formateur)
                ));
    }
    
    public long getTotalParticipants(){
        return formateurRepository.count();
    }
    
    public long countActiveFormations() {
        return formationRepository.countActiveFormations();
    }
    
    public long getTotalFormations() {
        return formationRepository.count();
    }
    
    public List<Formation> getMostPopularFormations() {
        return formationRepository.findAll().stream()
                .sorted(Comparator.comparingLong(Formation::getParticipantsCount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }
    
    public List<Formation> getMostPopularFormationsByYear(int year) {
        return formationRepository.findAll().stream()
                .filter(formation -> formation.getAnnee() == year)
                .sorted(Comparator.comparingLong(Formation::getParticipantsCount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }
    public Map<String, Long> getFormationsByDomaine() {
        return formationRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        formation -> formation.getDomaine().getLibelle(),
                        Collectors.counting()
                ));
    }
    public List<Map<String, Object>> getTopDomainsByBudget() {
        return formationRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        formation -> formation.getDomaine().getLibelle(),
                        Collectors.summingDouble(Formation::getBudget)
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .map(entry -> Map.<String, Object>of(
                        "domaine", entry.getKey(),
                        "totalBudget", entry.getValue()
                ))
                .collect(Collectors.toList());
    }
}
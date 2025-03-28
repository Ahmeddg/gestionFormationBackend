package tn.example.project_BD_PO.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.example.project_BD_PO.Entities.*;
import tn.example.project_BD_PO.Repositories.FormateurRepository;
import tn.example.project_BD_PO.Repositories.FormationRepository;

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
    public Formateur getBestFormateur() {
        return formateurRepository.findAll().stream()
                .max(Comparator.comparingLong(formationRepository::countByFormateur))
                .orElse(null);
    }
}
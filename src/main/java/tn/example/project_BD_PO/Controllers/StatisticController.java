package tn.example.project_BD_PO.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.example.project_BD_PO.Entities.Formateur;
import tn.example.project_BD_PO.Entities.Formation;
import tn.example.project_BD_PO.Services.StatisticService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/best-formations")
    public ResponseEntity<List<Formation>> getBestFormations() {
        return ResponseEntity.ok(statisticService.getBestFormations());
    }

    @GetMapping("/total-budget")
    public ResponseEntity<Long> getTotalFormationsBudget() {
        return ResponseEntity.ok(statisticService.getTotalFormationsBudget());
    }

    @GetMapping("/budget-by-year")
    public ResponseEntity<Map<Integer, Long>> getFormationsBudgetPerYear() {
        return ResponseEntity.ok(statisticService.getFormationsBudgetPerYear());
    }

    @GetMapping("/best-formateur")
    public ResponseEntity<Formateur> getBestFormateur() {
        return ResponseEntity.ok(statisticService.getBestFormateur());
    }
}
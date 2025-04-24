package tn.example.project_BD_PO.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.example.project_BD_PO.Entities.Formateur;
import tn.example.project_BD_PO.Entities.Formation;
import tn.example.project_BD_PO.Services.StatisticService;

import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;
    @PreAuthorize("hasRole('ADMINISTRATEUR') or hasRole('RESPONSABLE')")
    @GetMapping("/top-formations")
    public ResponseEntity<List<Formation>> getBestFormations() {
        return ResponseEntity.ok(statisticService.getBestFormations());
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR') or hasRole('RESPONSABLE')")
    @GetMapping("/total-budget")
    public ResponseEntity<Long> getTotalFormationsBudget() {
        return ResponseEntity.ok(statisticService.getTotalFormationsBudget());
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR') or hasRole('RESPONSABLE')")
    @GetMapping("/budget-by-year")
    public ResponseEntity<Map<Integer, Long>> getFormationsBudgetPerYear() {
        return ResponseEntity.ok(statisticService.getFormationsBudgetPerYear());
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR') or hasRole('RESPONSABLE')")
    @GetMapping("/top-formateurs")
    public ResponseEntity<Map<Formateur,Integer>> getBestFormateur() {
        return ResponseEntity.ok(statisticService.getBestFormateur());
    }


    @PreAuthorize("hasRole('ADMINISTRATEUR') or hasRole('RESPONSABLE')")
    @GetMapping("/total-participants")
    public ResponseEntity<Long> getTotalParticipants() {
        return ResponseEntity.ok(statisticService.getTotalParticipants());
    }
    @PreAuthorize("hasRole('ADMINISTRATEUR') or hasRole('RESPONSABLE')")
    @GetMapping("/active-formations")
    public ResponseEntity<Long> getActiveFormations() {
        return ResponseEntity.ok(statisticService.countActiveFormations());
    }
    @PreAuthorize("hasRole('ADMINISTRATEUR') or hasRole('RESPONSABLE')")
    @GetMapping("/total-formations")
    public ResponseEntity<Long> getTotalFormations() {
        return ResponseEntity.ok(statisticService.getTotalFormations());
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR') or hasRole('RESPONSABLE')")
    @GetMapping("/formations-by-domaine")
    public ResponseEntity<Map<String, Long>> getFormationsByDomaine() {
        return ResponseEntity.ok(statisticService.getFormationsByDomaine());
    }
    @PreAuthorize("hasRole('ADMINISTRATEUR') or hasRole('RESPONSABLE')")
    @GetMapping("/top-domains-by-budget")
    public ResponseEntity<List<Map<String, Object>>> getTopDomainsByBudget() {
        return ResponseEntity.ok(statisticService.getTopDomainsByBudget());
    }
}
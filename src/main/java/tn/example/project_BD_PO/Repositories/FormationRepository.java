package tn.example.project_BD_PO.Repositories;

import org.springframework.data.jpa.repository.Query;
import tn.example.project_BD_PO.Entities.Formateur;
import tn.example.project_BD_PO.Entities.Formation;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


@Repository
public interface FormationRepository extends JpaRepository<Formation, Integer> {
    long countByFormateur(Formateur formateur);
    @Query("SELECT f FROM Formation f ORDER BY SIZE(f.participants) DESC LIMIT 3")
    List<Formation> findMostPopularFormations();

    @Query("SELECT SUM(f.budget) FROM Formation f")
    Long sumAllBudgets();

    @Query("SELECT f.annee AS year, SUM(f.budget) AS totalBudget " +
            "FROM Formation f GROUP BY f.annee")
    List<BudgetByYear> sumBudgetByYear();

    interface BudgetByYear {
        Integer getYear();
        Long getTotalBudget();
    }
}

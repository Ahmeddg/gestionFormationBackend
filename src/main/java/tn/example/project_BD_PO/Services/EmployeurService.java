package tn.example.project_BD_PO.Services;

import tn.example.project_BD_PO.Entities.Employeur;
import tn.example.project_BD_PO.Repositories.EmployeurRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeurService {

    @Autowired
    private EmployeurRepository employeurRepository;

    // Retrieve all employeurs
    public List<Employeur> getAllEmployeurs() {
        return employeurRepository.findAll();
    }

    // Retrieve employeur by id
    public Optional<Employeur> getEmployeurById(Integer id) {
        return employeurRepository.findById(id);
    }

    // Save new or update existing employeur
    public Employeur saveEmployeur(Employeur employeur) {
        return employeurRepository.save(employeur);
    }

    // Delete employeur by id
    public void deleteEmployeur(Integer id) {
        employeurRepository.deleteById(id);
    }
}

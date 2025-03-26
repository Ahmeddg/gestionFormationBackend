package tn.example.project_BD_PO.Services;

import tn.example.project_BD_PO.Entities.Domaine;
import tn.example.project_BD_PO.Repositories.DomaineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DomaineService {

    @Autowired
    private DomaineRepository domaineRepository;

    // Retrieve all domaines
    public List<Domaine> getAllDomaines() {
        return domaineRepository.findAll();
    }

    // Retrieve domaine by id
    public Optional<Domaine> getDomaineById(Integer id) {
        return domaineRepository.findById(id);
    }

    // Save new or update existing domaine
    public Domaine saveDomaine(Domaine domaine) {
        return domaineRepository.save(domaine);
    }

    // Delete domaine by id
    public void deleteDomaine(Integer id) {
        domaineRepository.deleteById(id);
    }
}

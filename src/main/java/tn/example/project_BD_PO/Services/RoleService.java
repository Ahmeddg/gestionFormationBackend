package tn.example.project_BD_PO.Services;

import tn.example.project_BD_PO.Entities.Role;
import tn.example.project_BD_PO.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository RoleRepository;

    // Retrieve all Roles
    public List<Role> getAllRoles() {
        return RoleRepository.findAll();
    }

    // Retrieve Role by id
    public Optional<Role> getRoleById(Integer id) {
        return RoleRepository.findById(id);
    }

    // Save new or update existing Role
    public Role saveRole(Role Role) {
        return RoleRepository.save(Role);
    }

    // Delete Role by id
    public void deleteRole(Integer id) {
        RoleRepository.deleteById(id);
    }
}

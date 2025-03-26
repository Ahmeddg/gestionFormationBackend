package tn.example.project_BD_PO.Repositories;

import tn.example.project_BD_PO.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByNom(String roleNom);
}

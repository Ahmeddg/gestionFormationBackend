package tn.example.project_BD_PO.Repositories;

import tn.example.project_BD_PO.Entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    boolean existsByEmail(String email);
}

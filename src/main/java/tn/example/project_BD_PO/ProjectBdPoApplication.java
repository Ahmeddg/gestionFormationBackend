package tn.example.project_BD_PO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tn.example.project_BD_PO.Entities.Role;
import tn.example.project_BD_PO.Entities.Utilisateur;
import tn.example.project_BD_PO.Repositories.UtilisateurRepository;

@SpringBootApplication
public class ProjectBdPoApplication {

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProjectBdPoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializeAdmin() {
		return args -> {
			// Check if admin exists
			if (!utilisateurRepository.existsByRole(Role.ADMINISTRATEUR)) {
				// Create admin user
				Utilisateur admin = Utilisateur.builder()
						.username("admin")
						.password(new BCryptPasswordEncoder().encode("admin"))
						.role(Role.ADMINISTRATEUR)
						.build();
				
				utilisateurRepository.save(admin);
				System.out.println("Admin user created successfully with username: admin and password: admin");
			}
		};
	}
}

package tn.example.project_BD_PO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import tn.example.project_BD_PO.Entities.Role;
import tn.example.project_BD_PO.Entities.Utilisateur;
import tn.example.project_BD_PO.Repositories.UtilisateurRepository;


@SpringBootApplication
public class ProjectBdPoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectBdPoApplication.class, args);
	}
	@Bean
	public CommandLineRunner initAdminUser(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			boolean adminExists = utilisateurRepository.existsByRole(Role.ADMINISTRATEUR);
			if (!adminExists) {
				Utilisateur admin = Utilisateur.builder()
						.username("admin")
						.password(passwordEncoder.encode("admin"))
						.role(Role.ADMINISTRATEUR)
						.enabled(true)
						.accountNonExpired(true)
						.accountNonLocked(true)
						.credentialsNonExpired(true)
						.build();
				utilisateurRepository.save(admin);
				System.out.println("Default admin user created.");
				System.out.println("Default admin password: \"admin\"");

			} else {
				System.out.println("Admin user already exists.");
			}
		};
	}

}

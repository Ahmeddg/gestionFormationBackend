package tn.example.project_BD_PO.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.example.project_BD_PO.Entities.Utilisateur;
import tn.example.project_BD_PO.Repositories.UtilisateurRepository;
import tn.example.project_BD_PO.Services.UtilisateurService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UtilisateurService service;
    private final UtilisateurRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(Utilisateur request) {
        //check if the username already exists
        if (repository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        service.saveUtilisateur(request);
        var jwtToken = jwtService.generateToken(request);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(String.valueOf(request.getRole()))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Trim and validate username
        String username = request.getUsername().trim();
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        // Validate password (basic check)
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        try {
            // AuthenticationManager verifies credentials (username + password)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword())
            );

            // Additional explicit check (though AuthenticationManager already did this)
            Utilisateur user = repository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));


            // Generate token
            String jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .role(user.getRole().name())
                    .build();

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
    public Utilisateur getUserFromToken(String jwtToken) {
        String username = jwtService.extractUsername(jwtToken);
        return repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
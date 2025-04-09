package tn.example.project_BD_PO.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        service.saveUtilisateur(request);
        var jwtToken = jwtService.generateToken(request);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(String.valueOf(request.getRole()))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(String.valueOf(user.getRole()))
                .build();
    }
    public Utilisateur getUserFromToken(String jwtToken) {
        String username = jwtService.extractUsername(jwtToken);
        return repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
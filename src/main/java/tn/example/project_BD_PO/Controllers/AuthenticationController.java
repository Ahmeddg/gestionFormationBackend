package tn.example.project_BD_PO.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.example.project_BD_PO.Entities.Utilisateur;
import tn.example.project_BD_PO.Security.AuthenticationRequest;
import tn.example.project_BD_PO.Security.AuthenticationResponse;
import tn.example.project_BD_PO.Security.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody Utilisateur request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/user-info")
    public ResponseEntity<Utilisateur> getUserInfo( @RequestHeader(value = "Authorization" ,required = false) String authHeader) {
        String jwtToken = authHeader.substring(7);
        Utilisateur user = authenticationService.getUserFromToken(jwtToken);
        return ResponseEntity.ok(user);
    }
}
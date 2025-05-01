package tn.example.project_BD_PO.Controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import tn.example.project_BD_PO.Entities.Utilisateur;
import tn.example.project_BD_PO.Security.AuthenticationRequest;
import tn.example.project_BD_PO.Security.AuthenticationResponse;
import tn.example.project_BD_PO.Security.AuthenticationService;
import tn.example.project_BD_PO.Security.ErrorResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody Utilisateur request
    ) {
        try {
            AuthenticationResponse response = authenticationService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .body(response);
        } catch (Exception ex) {
            log.error("Registration error for user: {}", request.getUsername(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.createBasicError(
                            HttpStatus.BAD_REQUEST,
                            "Registration failed: " + ex.getMessage(),
                            "REGISTRATION_ERROR"
                    ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .header("X-Content-Type-Options", "nosniff")
                    .header("X-Frame-Options", "DENY")
                    .body(response);
        } catch (AuthenticationException ex) {
            log.warn("Authentication failed for user: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.createBasicError(
                            HttpStatus.UNAUTHORIZED,
                            "Invalid credentials: " + ex.getMessage(),
                            "AUTHENTICATION_FAILED"
                    ));
        } catch (Exception ex) {
            log.error("Login error for user: {}", request.getUsername(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.createBasicError(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Internal server error: " + ex.getMessage(),
                            "SERVER_ERROR"
                    ));
        }
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.createBasicError(
                            HttpStatus.UNAUTHORIZED,
                            "Missing or invalid authorization header",
                            "INVALID_AUTH_HEADER"
                    ));
        }

        try {
            String jwtToken = authHeader.substring(7);
            Utilisateur user = authenticationService.getUserFromToken(jwtToken);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .body(user);
        } catch (Exception ex) {
            log.error("Error fetching user info", ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.createBasicError(
                            HttpStatus.UNAUTHORIZED,
                            "Invalid or expired token: " + ex.getMessage(),
                            "INVALID_TOKEN"
                    ));
        }
    }
}
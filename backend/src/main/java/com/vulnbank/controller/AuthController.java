package com.vulnbank.controller;

import com.vulnbank.model.User;
import com.vulnbank.service.AuthService;
import com.vulnbank.util.RedirectUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            Optional<User> userOpt = authService.login(email, password);
            if (userOpt.isPresent()) {
                String token = authService.generateToken(email);
                return ResponseEntity.ok(Map.of("token", token, "user", userOpt.get()));
            }
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // VULN-ID: VB-013 | CWE-601 | Severity: MEDIUM
    // Open Redirect delegated to RedirectUtil – no origin validation on 'next' param.
    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RedirectUtil.handleLogoutRedirect(request, response);
    }
}

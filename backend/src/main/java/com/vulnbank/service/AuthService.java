package com.vulnbank.service;

import com.vulnbank.model.User;
import com.vulnbank.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    // VULN-ID: VB-008 | CWE-798 | Severity: CRITICAL
    // Hardcoded JWT signing secret in source code.
    // SAFE VERSION: load from environment variable or secrets manager:
    //   private final String jwtSecret = System.getenv("JWT_SECRET");
    private static final String JWT_SECRET = "vulnbank-hardcoded-secret-9876";

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // VULN-ID: VB-007 | CWE-327 | Severity: HIGH
    // Weak cryptography: password hashed with MD5, which is broken and reversible via rainbow tables.
    // SAFE VERSION: use BCryptPasswordEncoder with strength 12:
    //   BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    //   String passwordHash = encoder.encode(password);
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86_400_000))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
        // SAFE VERSION: load JWT_SECRET from environment variable at startup and
        // use Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)).
    }

    public Optional<User> login(String email, String password) throws NoSuchAlgorithmException {
        String hashed = hashPassword(password);
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(hashed));
    }
}

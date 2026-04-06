package com.vulnbank.controller;

import com.vulnbank.model.User;
import com.vulnbank.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // VULN-ID: VB-003 | CWE-915 | Severity: HIGH
    // Mass Assignment: the entire User entity is bound directly from the request body.
    // An attacker can POST {"email":"x","password":"y","role":"ROLE_ADMIN"} to
    // self-assign admin privileges.
    // SAFE VERSION (commented out):
    //   public ResponseEntity<User> register(@RequestBody RegisterRequest dto) {
    //       User user = new User();
    //       user.setEmail(dto.getEmail());
    //       user.setPassword(dto.getPassword());
    //       user.setFullName(dto.getFullName());
    //       // role is never set from request
    //       return ResponseEntity.ok(userRepository.save(user));
    //   }
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    // VULN-ID: VB-004 | CWE-22 | Severity: HIGH
    // Path Traversal: user-supplied filename is concatenated directly onto a base path.
    // Attacker can supply ../../etc/passwd to read arbitrary files.
    // SAFE VERSION (commented out):
    //   if (!filename.matches("^[a-zA-Z0-9_\\-]+\\.csv$")) {
    //       return ResponseEntity.badRequest().body("Invalid filename");
    //   }
    @GetMapping("/export")
    public ResponseEntity<?> exportFile(HttpServletRequest request) {
        try {
            String filename = request.getParameter("filename");
            File file = new File("/var/data/exports/" + filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = fis.readAllBytes();
            fis.close();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + filename)
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

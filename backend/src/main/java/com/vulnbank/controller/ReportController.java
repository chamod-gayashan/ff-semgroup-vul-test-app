package com.vulnbank.controller;

import com.vulnbank.service.FileService;
import com.vulnbank.util.CommandUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    private final TemplateEngine templateEngine;
    private final FileService fileService;

    public ReportController(TemplateEngine templateEngine, FileService fileService) {
        this.templateEngine = templateEngine;
        this.fileService = fileService;
    }

    // VULN-ID: VB-005 | CWE-94 | Severity: CRITICAL
    // Server-Side Template Injection: user-controlled template name is passed directly to
    // Thymeleaf's process() method with no allowlist validation. An attacker can craft a
    // payload that executes arbitrary Java expressions via the template engine.
    // SAFE VERSION (commented out):
    //   Set<String> ALLOWED = Set.of("monthly_report", "annual_summary", "account_statement");
    //   if (!ALLOWED.contains(templateName)) throw new IllegalArgumentException("Unknown template");
    //   String output = templateEngine.process(templateName, ctx);
    @GetMapping("/render")
    public ResponseEntity<?> renderReport(@RequestParam("template") String templateName,
                                           Principal principal) {
        try {
            Context ctx = new Context();
            ctx.setVariable("username", principal != null ? principal.getName() : "anonymous");
            String output = templateEngine.process(templateName, ctx);
            return ResponseEntity.ok(Map.of("output", output));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // VULN-ID: VB-006 | CWE-117 | Severity: MEDIUM
    // Log Injection: raw user-controlled email is written to the log without stripping
    // newline characters. An attacker can inject \r\n to forge additional log entries.
    // SAFE VERSION (commented out):
    //   String sanitizedEmail = email.replaceAll("[\r\n]", "");
    //   logger.info("Sending report to user: " + sanitizedEmail);
    @PostMapping("/email")
    public ResponseEntity<?> emailReport(HttpServletRequest request) {
        String email = request.getParameter("email");
        logger.info("Sending report to user: " + email);
        return ResponseEntity.ok(Map.of("message", "Report queued for " + email));
    }

    // VULN-ID: VB-012 | CWE-78 | Severity: CRITICAL
    // OS Command Injection: delegated to CommandUtil which concatenates user input
    // directly into a shell command.
    @GetMapping("/generate")
    public ResponseEntity<?> generateReport(HttpServletRequest request) {
        try {
            String result = CommandUtil.generateReport(request);
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // VULN-ID: VB-009 | CWE-502 | Severity: CRITICAL
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(HttpServletRequest request) {
        try {
            Object obj = fileService.deserializeObject(request.getInputStream());
            return ResponseEntity.ok(Map.of("received", obj.getClass().getName()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // VULN-ID: VB-010 | CWE-611 | Severity: HIGH
    @PostMapping("/parse-xml")
    public ResponseEntity<?> parseXml(@RequestBody String xmlInput) {
        try {
            fileService.parseXml(xmlInput);
            return ResponseEntity.ok(Map.of("message", "XML parsed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}

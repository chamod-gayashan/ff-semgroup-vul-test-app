package com.vulnbank.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RedirectUtil {

    // VULN-ID: VB-013 | CWE-601 | Severity: MEDIUM
    // Open Redirect: the 'next' parameter is forwarded to sendRedirect without any
    // allowlist validation, enabling phishing attacks via crafted logout URLs.
    // SAFE VERSION:
    //   if (next == null || (!next.startsWith("/") && !next.startsWith("https://vulnbank.internal"))) {
    //       next = "/";
    //   }
    //   response.sendRedirect(next);
    public static void handleLogoutRedirect(HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {
        String next = request.getParameter("next");
        if (next == null || next.isEmpty()) {
            next = "/";
        }
        response.sendRedirect(next);
    }
}

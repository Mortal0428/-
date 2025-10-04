package com.example.aid.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = "/";
        boolean isAdmin = false;
        boolean isStudent = false;
        boolean isCounselor = false;
        boolean isDean = false;
        boolean isGroup = false;

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            if ("ROLE_ADMIN".equals(role)) isAdmin = true;
            if ("ROLE_STUDENT".equals(role)) isStudent = true;
            if ("ROLE_COUNSELOR".equals(role)) isCounselor = true;
            if ("ROLE_DEAN".equals(role)) isDean = true;
            if ("ROLE_GROUP".equals(role)) isGroup = true;
        }

        if (isAdmin) {
            redirectUrl = "/admin";
        } else if (isStudent) {
            redirectUrl = "/student/home";
        } else if (isCounselor) {
            redirectUrl = "/counselor/home";
        } else if (isGroup) {
            redirectUrl = "/group/review";
        } else if (isDean) {
            redirectUrl = "/dean/review";
        } else {
            redirectUrl = "/login";
        }
        response.sendRedirect(redirectUrl);
    }
}

package com.example.news.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Your custom JSON format
        Map<String, Object> error = new HashMap<>();
        error.put("status", false);
        error.put("message", "Unauthorized");

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), error);
    }
}

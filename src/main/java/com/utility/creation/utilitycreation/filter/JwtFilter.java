package com.utility.creation.utilitycreation.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utility.creation.utilitycreation.exceptions.customexception.CustomUsernameNotFoundException;
import com.utility.creation.utilitycreation.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter  extends OncePerRequestFilter{
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        
        // Extract JWT token and username from the header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }
        
        if (username != null) {
            try {
                // Attempt to load user by username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // If token is valid, authenticate user
                if (jwtUtil.validateToken(jwt)) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (CustomUsernameNotFoundException ex) {
                // Handle the exception and return custom error response
                handleException(response, HttpServletResponse.SC_NOT_FOUND, "Not Found", ex.getMessage());
                return; // Stop further processing if exception occurs
            }
        }
        
        // Continue with the filter chain if no exception
        chain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, int status, String error, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        
        // Build the response dynamically
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status);
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", Instant.now());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
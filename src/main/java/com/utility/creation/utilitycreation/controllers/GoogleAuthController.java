package com.utility.creation.utilitycreation.controllers;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.utility.creation.utilitycreation.exceptions.customexception.ResourceNotFoundException;
import com.utility.creation.utilitycreation.model.entity.Roles;
import com.utility.creation.utilitycreation.model.entity.UserRoleMapping;
import com.utility.creation.utilitycreation.repository.RoleRepository;
import com.utility.creation.utilitycreation.repository.UserRoleMappingRepository;
import com.utility.creation.utilitycreation.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.utility.creation.utilitycreation.common.service.UserDetailsServiceImpl;
import com.utility.creation.utilitycreation.model.entity.User;
import com.utility.creation.utilitycreation.repository.UserRepo;
import com.utility.creation.utilitycreation.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/google")
@Slf4j
public class GoogleAuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleMappingRepository userRoleMappingRepository;

    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

    @GetMapping("/oauth/login")
    public String googleLogin() {
        String url = GOOGLE_AUTH_URL +
                "?client_id=" + clientId +
                "&redirect_uri=" + "http://localhost:8080/google/oauth/callback" +
                "&response_type=code" +
                "&scope=email profile";

        return "Redirect to: <a href=\"" + url + "\">Google Login</a>";
    }

    @GetMapping("/oauth/callback")
    public ResponseEntity<String> handleGoogleCallback(@RequestParam String code) {
        try {
            String tokenEndpoint = GOOGLE_TOKEN_URL;
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", "http://localhost:8080/google/oauth/callback");
            params.add("grant_type", "authorization_code");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            String idToken = (String) tokenResponse.getBody().get("id_token");
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);
            if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                UserDetails userDetails = null;
                try {
                    userDetails = userDetailsService.loadUserByUsername(email);
                } catch (Exception e) {
                    User user = new User();
                    user.setId(String.valueOf(Math.abs(UUID.randomUUID().hashCode())));
                    user.setEmail(email);
                    user.setFirstName(email);
                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    Roles defaultRole = roleRepository.findByRoleName("USER");
                    if (defaultRole == null) {
                        throw new ResourceNotFoundException("Default role not found in the database!");
                    }
                    userRepository.save(user);
                    UserRoleMapping mapping = registerService.createRoleMapping(user, defaultRole);
                    userRoleMappingRepository.save(mapping);
                }
                String jwtToken = jwtUtil.generateToken(email);
                System.out.println("Token :::::::: " + jwtToken);

                // Store JWT token in localStorage via welcome.html script
                return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(
                                        "<script>" +
                                        "localStorage.setItem('email', '" + email + "');" +
                                        "localStorage.setItem('jwtToken', '" + jwtToken + "');" +
                                        "window.location.href = '/welcome.html';" +
                                        "</script>"
                                );
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("Exception occurred while handleGoogleCallback ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

/*
 * 
 * https://accounts.google.com/o/oauth2/auth?
 * client_id=YOUR_CLIENT_ID
 * &redirect_uri=YOUR_REDIRECT_URI
 * &response_type=code
 * &scope=email profile
 * &access_type=offline
 * &prompt=consent
 * 
 */
package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public boolean authenticate(String email, String password) {
        logger.info("Attempting login for user: {}", email);
        
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            logger.info("User logged in successfully: {}", email);
            return true;
        } catch (Exception e) {
            logger.warn("Login failed for user: {}", email);
            return false;
        }
    }

    public User register(String firstName, String lastName, String email, String password) {
        logger.info("Starting registration process for user: {}", email);
        
        if (userRepository.existsByEmail(email)) {
            logger.warn("Registration failed: email {} already exists", email);
            throw new RuntimeException("Email is already in use");
        }

        try {
            var user = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .role(Role.USER)
                    .build();
            User savedUser = userRepository.save(user);
            logger.info("User registered successfully: {}", savedUser.getEmail());
            return savedUser;
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            throw new RuntimeException("Ошибка при сохранении пользователя: " + e.getMessage());
        }
    }
} 
package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User register(User user) {
        logger.info("Starting registration process for user: {}", user.getUsername());
        
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.warn("Registration failed: username {} already exists", user.getUsername());
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.warn("Registration failed: email {} already exists", user.getEmail());
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            logger.info("User registered successfully: {}", savedUser.getUsername());
            return savedUser;
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            throw new RuntimeException("Ошибка при сохранении пользователя: " + e.getMessage());
        }
    }

    public User login(String username, String password) {
        logger.info("Attempting login for user: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Login failed: user {} not found", username);
                    return new RuntimeException("Пользователь не найден");
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Login failed: incorrect password for user {}", username);
            throw new RuntimeException("Неверный пароль");
        }

        logger.info("User logged in successfully: {}", username);
        return user;
    }
} 
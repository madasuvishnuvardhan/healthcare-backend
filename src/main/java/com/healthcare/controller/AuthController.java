package com.healthcare.controller;

import com.healthcare.model.User;
import com.healthcare.repository.UserRepository;
import com.healthcare.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
        }
        userRepo.save(user);
        return Map.of("message", "User registered");
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        User existingUser = userRepo.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            String token = jwtService.generateToken(user.getEmail());
            return Map.of("token", token);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}

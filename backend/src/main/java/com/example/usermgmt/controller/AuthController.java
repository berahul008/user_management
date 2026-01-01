
package com.example.usermgmt.controller;

import com.example.usermgmt.dto.AuthResponse;
import com.example.usermgmt.dto.LoginRequest;
import com.example.usermgmt.dto.RegisterRequest;
import com.example.usermgmt.model.User;
import com.example.usermgmt.repo.UserRepository;
import com.example.usermgmt.security.JwtService;
import com.example.usermgmt.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

  private final UserService userService;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;
  private final UserRepository userRepository;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
    User u = userService.register(req);
    String token = jwtService.generateToken(u.getUsername());
    return ResponseEntity.ok(new AuthResponse(token, u.getId(), u.getUsername(), u.getEmail()));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
    authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
    User u = userRepository.findByUsername(req.getUsername()).orElseThrow();
    String token = jwtService.generateToken(u.getUsername());
    return ResponseEntity.ok(new AuthResponse(token, u.getId(), u.getUsername(), u.getEmail()));
  }
}

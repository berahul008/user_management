
package com.example.usermgmt.service;

import com.example.usermgmt.dto.RegisterRequest;
import com.example.usermgmt.model.User;
import com.example.usermgmt.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User register(RegisterRequest req) {
    if (userRepository.existsByUsername(req.getUsername())) {
      throw new IllegalArgumentException("Username already exists");
    }
    if (userRepository.existsByEmail(req.getEmail())) {
      throw new IllegalArgumentException("Email already exists");
    }
    User u = User.builder()
        .username(req.getUsername())
        .email(req.getEmail())
        .password(passwordEncoder.encode(req.getPassword()))
        .build();
    u.getRoles().add("USER");
    return userRepository.save(u);
  }

  public List<User> listAll() {
    return userRepository.findAll();
  }

  public User getById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
  }

  public void deleteById(Long id) {
    userRepository.deleteById(id);
  }

  @Transactional
  public void addFriend(Long currentUserId, Long friendId) {
    if (currentUserId.equals(friendId)) {
      throw new IllegalArgumentException("Cannot friend yourself");
    }
    User me = getById(currentUserId);
    User friend = getById(friendId);
    me.addFriend(friend); // ensures bidirectional
  }

  @Transactional
  public void removeFriend(Long currentUserId, Long friendId) {
    User me = getById(currentUserId);
    User friend = getById(friendId);
    me.removeFriend(friend); // bidirectional removal
  }
}

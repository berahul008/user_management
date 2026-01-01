
package com.example.usermgmt.controller;

import com.example.usermgmt.model.User;
import com.example.usermgmt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<User>> list() {
    return ResponseEntity.ok(userService.listAll());
  }

  @GetMapping("/me")
  public ResponseEntity<User> me(Authentication auth) {
    String username = auth.getName();
    return ResponseEntity.ok(
        userService.listAll().stream().filter(u -> u.getUsername().equals(username)).findFirst().orElseThrow()
    );
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
    // Demo rule: allow self-delete or role ADMIN
    User me = userService.listAll().stream().filter(u -> u.getUsername().equals(auth.getName())).findFirst().orElseThrow();
    boolean isAdmin = me.getRoles().contains("ADMIN");
    if (!isAdmin && !me.getId().equals(id)) {
      return ResponseEntity.status(403).build();
    }
    userService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}

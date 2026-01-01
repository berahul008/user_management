
package com.example.usermgmt.controller;

import com.example.usermgmt.model.User;
import com.example.usermgmt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@CrossOrigin
public class FriendController {

  private final UserService userService;

  @PostMapping("/{friendId}")
  public ResponseEntity<Void> addFriend(@PathVariable Long friendId, Authentication auth) {
    Long myId = userService.listAll().stream().filter(u -> u.getUsername().equals(auth.getName())).findFirst().orElseThrow().getId();
    userService.addFriend(myId, friendId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{friendId}")
  public ResponseEntity<Void> removeFriend(@PathVariable Long friendId, Authentication auth) {
    Long myId = userService.listAll().stream().filter(u -> u.getUsername().equals(auth.getName())).findFirst().orElseThrow().getId();
    userService.removeFriend(myId, friendId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me")
  public ResponseEntity<Set<User>> myFriends(Authentication auth) {
    User me = userService.listAll().stream().filter(u -> u.getUsername().equals(auth.getName())).findFirst().orElseThrow();
    return ResponseEntity.ok(me.getFriends());
  }
}

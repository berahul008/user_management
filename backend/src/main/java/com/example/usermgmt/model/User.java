
package com.example.usermgmt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  @NotBlank
  private String username;

  @Column(unique = true, nullable = false)
  @Email
  private String email;

  @Column(nullable = false)
  private String password; // BCrypt hash

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "role")
  private Set<String> roles = new HashSet<>();

  // Self-referencing ManyToMany for friends; store both directions
  @ManyToMany
  @JoinTable(
      name = "user_friends",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "friend_id")
  )
  private Set<User> friends = new HashSet<>();

  public void addFriend(User other) {
    if (other == null || other.equals(this)) return;
    this.friends.add(other);
    other.friends.add(this);
  }

  public void removeFriend(User other) {
    if (other == null || other.equals(this)) return;
    this.friends.remove(other);
    other.friends.remove(this);
  }
}

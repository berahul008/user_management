
package com.example.usermgmt.security;

import com.example.usermgmt.repo.UserRepository;
import com.example.usermgmt.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain) throws ServletException, IOException {

    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      try {
        String username = jwtService.extractUsername(token);

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
          User user = userOpt.get();

          Set<SimpleGrantedAuthority> authorities = user.getRoles()
              .stream()
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toSet());

          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(username, null, authorities);

          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (Exception ignored) {
        // Invalid token: leave request unauthenticated
      }
    }
    chain.doFilter(request, response);
  }
}

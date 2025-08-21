package com.example.proyecto.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  private final UsuarioDetailsService uds;
  public SecurityConfig(UsuarioDetailsService uds) { this.uds = uds; }

  @Bean
  public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

  @Bean
  public RoleBasedAuthSuccessHandler roleSuccessHandler() { return new RoleBasedAuthSuccessHandler(); }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
      .headers(h -> h.frameOptions(f -> f.sameOrigin()))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/home", "/login", "/h2-console/**", "/css/**", "/js/**", "/img/**").permitAll()
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .requestMatchers("/usuario/perfil", "/prestamos/mis-libros").hasAnyRole("USER","ADMIN")
        .requestMatchers(HttpMethod.POST, "/prestamos/devolver/**").hasAnyRole("USER","ADMIN")
        .requestMatchers("/libros/**", "/prestamos/**").hasRole("ADMIN")
        .anyRequest().authenticated()
      )
      .formLogin(login -> login
        .loginPage("/login").permitAll()
        .successHandler(roleSuccessHandler())     // ← redirección por rol
      )
      .exceptionHandling(e -> e.accessDeniedPage("/access-denied"))
      .logout(l -> l.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll())
      .userDetailsService(uds);

    return http.build();
  }
}

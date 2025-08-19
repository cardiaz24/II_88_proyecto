package com.example.proyecto.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UsuarioDetailsService uds;

  public SecurityConfig(UsuarioDetailsService uds, CustomSuccessHandler successHandler) {
  this.uds = uds;
  this.successHandler = successHandler;
}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

private final CustomSuccessHandler successHandler;

   @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  http
    .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
    .headers(h -> h.frameOptions(f -> f.sameOrigin()))
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/h2-console/**", "/css/**", "/js/**", "/img/**").permitAll()
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .requestMatchers("/usuario/perfil", "/prestamos/mis-libros").hasAnyRole("USER", "ADMIN")
        .requestMatchers(org.springframework.http.HttpMethod.POST, "/prestamos/devolver/**")
        .hasAnyRole("USER", "ADMIN")
        .requestMatchers("/libros/**", "/prestamos/**").hasRole("ADMIN")
        .anyRequest().authenticated())
    .formLogin(login -> login
        .loginPage("/login")
        .permitAll()
        .successHandler(successHandler)) 
    .exceptionHandling(e -> e.accessDeniedPage("/access-denied"))
    .logout(l -> l.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll())
    .userDetailsService(uds);

  return http.build();
}
}

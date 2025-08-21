package com.example.proyecto.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleBasedAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  private final RequestCache requestCache = new HttpSessionRequestCache();

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    SavedRequest saved = requestCache.getRequest(request, response);
    if (saved != null) {
      super.onAuthenticationSuccess(request, response, authentication);
      return;
    }

    Set<String> roles = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());

    if (roles.contains("ROLE_ADMIN")) {
      getRedirectStrategy().sendRedirect(request, response, "/libros");
    } else {
      getRedirectStrategy().sendRedirect(request, response, "/prestamos/mis-libros");
    }
  }
}

package com.example.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/usuario")
public class UsuarioController {
  @GetMapping("/perfil")
  public String perfil(){ return "usuario/perfil"; }
}
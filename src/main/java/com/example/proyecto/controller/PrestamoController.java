package com.example.proyecto.controller;

import com.example.proyecto.model.*;
import com.example.proyecto.repository.*;
import com.example.proyecto.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {
  private final PrestamoRepository prestamoRepo;
  private final UsuarioRepository usuarioRepo;
  private final EjemplarRepository ejemplarRepo;
  private final PrestamoService prestamoService;

  public PrestamoController(PrestamoRepository pr, UsuarioRepository ur, EjemplarRepository er, PrestamoService ps) {
    this.prestamoRepo = pr;
    this.usuarioRepo = ur;
    this.ejemplarRepo = er;
    this.prestamoService = ps;
  }

  // ADMIN: listado general
  @GetMapping
  public String lista(Model model) {
    model.addAttribute("prestamos", prestamoRepo.findAll());
    return "prestamos/lista";
  }

  // USER/ADMIN: mis libros
 @GetMapping("/mis-libros")
public String misLibros(@AuthenticationPrincipal User user, Model model) {
    var u = usuarioRepo.findByUsername(user.getUsername()).orElseThrow();
    model.addAttribute("prestamos", prestamoRepo.findByUsuario_Id(u.getId()));
    return "prestamos/mis-libros";
}


  // ADMIN: crear préstamo
  @GetMapping("/nuevo")
  public String nuevo(Model model) {
    model.addAttribute("prestamo", new Prestamo());
    model.addAttribute("ejemplares", ejemplarRepo.findAll());
    model.addAttribute("usuarios", usuarioRepo.findAll());
    return "prestamos/formulario";
  }

  @PostMapping
  public String crear(@Valid @ModelAttribute("prestamo") Prestamo pr,
      BindingResult br, RedirectAttributes ra) {
    if (br.hasErrors())
      return "prestamos/formulario";
    var u = usuarioRepo.findById(pr.getUsuario().getId()).orElseThrow();
    var e = ejemplarRepo.findById(pr.getEjemplar().getId()).orElseThrow();
    pr = prestamoService.prestar(u, e);
    ra.addFlashAttribute("ok", "Préstamo creado");
    return "redirect:/prestamos";
  }

  // ADMIN/USER: devolver
  @PostMapping("/devolver/{id}")
  public String devolver(@PathVariable Long id, RedirectAttributes ra) {
    var pr = prestamoRepo.findById(id).orElseThrow();
    prestamoService.devolver(pr);
    ra.addFlashAttribute("ok", "Préstamo devuelto");
    return "redirect:/prestamos";
  }

  // ADMIN: limpiar multa
  @PostMapping("/limpiar-multa/{id}")
  public String limpiarMulta(@PathVariable Long id, RedirectAttributes ra) {
    var pr = prestamoRepo.findById(id).orElseThrow();
    prestamoService.limpiarMulta(pr);
    ra.addFlashAttribute("ok", "Multa limpiada");
    return "redirect:/prestamos";
  }

  // ADMIN: eliminar préstamo
  @PostMapping("/eliminar/{id}")
  public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
    prestamoRepo.deleteById(id);
    ra.addFlashAttribute("ok", "Préstamo eliminado");
    return "redirect:/prestamos";
  }

@GetMapping("/historial")
public String historial(@AuthenticationPrincipal User user, Model model) {
    var u = usuarioRepo.findByUsername(user.getUsername()).orElseThrow();
    var prestamos = prestamoRepo.findByUsuario_IdOrderByFechaPrestamoDesc(u.getId());
    model.addAttribute("prestamos", prestamos);
    return "prestamos/historial";
}


}

package com.example.proyecto.controller;

import com.example.proyecto.model.Libro;
import com.example.proyecto.repository.AutorRepository;
import com.example.proyecto.repository.CategoriaRepository;
import com.example.proyecto.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/libros")
public class LibroController {
  private final LibroService libroService;
  private final AutorRepository autorRepo;
  private final CategoriaRepository categoriaRepo;

  public LibroController(LibroService s, AutorRepository a, CategoriaRepository c) {
    this.libroService = s;
    this.autorRepo = a;
    this.categoriaRepo = c;
  }

  @GetMapping
  public String listar(Model model) {
    model.addAttribute("libros", libroService.findAll());
    return "libros/lista";
  }

  @GetMapping("/nuevo")
  public String nuevo(Model model) {
    model.addAttribute("libro", new Libro());
    model.addAttribute("autores", autorRepo.findAll());
    model.addAttribute("categorias", categoriaRepo.findAll());
    return "libros/formulario";
  }

  @PostMapping
  public String guardar(@Valid @ModelAttribute("libro") Libro libro,
      BindingResult br,
      Model model,
      RedirectAttributes ra) {
    if (br.hasErrors()) {
      model.addAttribute("autores", autorRepo.findAll());
      model.addAttribute("categorias", categoriaRepo.findAll());
      return "libros/formulario";
    }
    libroService.save(libro);
    ra.addFlashAttribute("ok", "Libro guardado correctamente");
    return "redirect:/libros";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
    var opt = libroService.findById(id);
    if (opt.isEmpty()) {
      ra.addFlashAttribute("error", "Libro no existe");
      return "redirect:/libros";
    }
    model.addAttribute("libro", opt.get());
    model.addAttribute("autores", autorRepo.findAll());
    model.addAttribute("categorias", categoriaRepo.findAll());
    return "libros/formulario";
  }

  @PostMapping("/eliminar/{id}")
  public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
    libroService.deleteById(id);
    ra.addFlashAttribute("ok", "Libro eliminado");
    return "redirect:/libros";
  }
}

package com.example.proyecto.controller;

import com.example.proyecto.model.Libro;
import com.example.proyecto.model.Autor;
import com.example.proyecto.model.Categoria;
import com.example.proyecto.service.LibroService;
import com.example.proyecto.service.AutorService;
import com.example.proyecto.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/libros")
public class LibroController {
    private final LibroService libroService;
    private final AutorService AutorService;
    private final CategoriaService CategoriaService;

    public LibroController(LibroService libroService, AutorService autorService, 
                         CategoriaService categoriaService) {
        this.libroService = libroService;
        this.AutorService = autorService;
        this.CategoriaService = categoriaService;
    }

    @GetMapping
    public String listarLibros(Model model) {
        model.addAttribute("libros", libroService.obtenerTodos());
        return "libros/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("libro", new Libro());
        cargarDatosFormulario(model);
        return "libros/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Libro libro = libroService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
        
        model.addAttribute("libro", libro);
        cargarDatosFormulario(model);
        return "libros/formulario";
    }

@PostMapping
public String guardarLibro(@Valid @ModelAttribute("libro") Libro libro,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {

    if (result.hasErrors()) {
        cargarDatosFormulario(model);
        return "libros/formulario";
    }

    try {
       Libro libroGuardado = libroService.guardar(libro);

redirectAttributes.addFlashAttribute("success",
    (libroGuardado.getId() != null ? "Libro actualizado correctamente" : "Libro creado correctamente"));

return "redirect:/libros";

    } catch (IllegalArgumentException ex) {
        model.addAttribute("error", ex.getMessage());
        cargarDatosFormulario(model);
        return "libros/formulario";
    } catch (Exception e) {
        model.addAttribute("error", "Error inesperado al guardar el libro.");
        cargarDatosFormulario(model);
        return "libros/formulario";
    }
}

    @PostMapping("/eliminar/{id}")
    public String eliminarLibro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            libroService.eliminarPorId(id);
            redirectAttributes.addFlashAttribute("success", "Libro eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar libro: " + e.getMessage());
        }
        return "redirect:/libros";
    }

    private void cargarDatosFormulario(Model model) {
        List<Autor> autores = AutorService.findAll();
        List<Categoria> categorias = CategoriaService.findAll();
        
        model.addAttribute("autores", autores);
        model.addAttribute("categorias", categorias);
    }
}
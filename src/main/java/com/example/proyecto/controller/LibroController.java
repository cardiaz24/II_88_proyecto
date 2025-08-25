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
    private final AutorService autorService;
    private final CategoriaService categoriaService;

    public LibroController(LibroService libroService, AutorService autorService, 
                         CategoriaService categoriaService) {
        this.libroService = libroService;
        this.autorService = autorService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listarLibros(Model model) {
        model.addAttribute("libros", libroService.findAll());
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
        Libro libro = libroService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
        
        model.addAttribute("libro", libro);
        cargarDatosFormulario(model);
        return "libros/formulario";
    }

    @PostMapping
    public String guardarLibro(@Valid @ModelAttribute Libro libro, 
                              BindingResult result, 
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            return "libros/formulario";
        }
        
        try {
            libroService.save(libro);
            redirectAttributes.addFlashAttribute("success", 
                libro.getId() != null ? "Libro actualizado correctamente" : "Libro creado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            cargarDatosFormulario(model);
            return "libros/formulario";
        }
        
        return "redirect:/libros";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarLibro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            libroService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Libro eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar libro: " + e.getMessage());
        }
        return "redirect:/libros";
    }

    private void cargarDatosFormulario(Model model) {
        List<Autor> autores = autorService.findAll();
        List<Categoria> categorias = categoriaService.findAll();
        
        model.addAttribute("autores", autores);
        model.addAttribute("categorias", categorias);
    }
}
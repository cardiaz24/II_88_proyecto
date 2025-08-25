package com.example.proyecto.controller;

import com.example.proyecto.model.Autor;
import com.example.proyecto.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public String listarAutores(Model model) {
        List<Autor> autores = autorService.findAll();
        model.addAttribute("autores", autores);
        return "autores/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("autor", new Autor());
        return "autores/formulario";
    }

    @PostMapping
    public String guardarAutor(@Valid @ModelAttribute Autor autor,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        if (result.hasErrors()) {
            return "autores/formulario";
        }

        // Validación de duplicado
        if (autorService.existePorNombreYPais(autor.getNombre(), autor.getPais())) {
            model.addAttribute("error", "Ya existe un autor con el mismo nombre y país.");
            return "autores/formulario";
        }

        autorService.save(autor);
        redirectAttributes.addFlashAttribute("success", "Autor guardado correctamente.");
        return "redirect:/autores";
    }

    @GetMapping("/editar/{id}")
    public String editarAutor(@PathVariable Long id, Model model) {
        Autor autor = autorService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Autor no encontrado"));
        model.addAttribute("autor", autor);
        return "autores/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarAutor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            autorService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Autor eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el autor: " + e.getMessage());
        }
        return "redirect:/autores";
    }
}

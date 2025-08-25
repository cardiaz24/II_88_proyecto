package com.example.proyecto.controller;

import com.example.proyecto.model.Prestamo;
import com.example.proyecto.model.Usuario;
import com.example.proyecto.model.Ejemplar;
import com.example.proyecto.service.PrestamoService;
import com.example.proyecto.service.UsuarioService;
import com.example.proyecto.service.EjemplarService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {
    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService;
    private final EjemplarService ejemplarService;

    public PrestamoController(PrestamoService prestamoService, UsuarioService usuarioService,
            EjemplarService ejemplarService) {
        this.prestamoService = prestamoService;
        this.usuarioService = usuarioService;
        this.ejemplarService = ejemplarService;
    }

    @GetMapping
    public String listarPrestamos(Model model) {
        model.addAttribute("prestamos", prestamoService.findAll());
        return "prestamos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("prestamo", new Prestamo());
        cargarDatosFormulario(model);
        return "prestamos/formulario";
    }

    @PostMapping
    public String crearPrestamo(@ModelAttribute Prestamo prestamo,
            RedirectAttributes redirectAttributes) {
        try {
            prestamoService.prestar(prestamo.getUsuario(), prestamo.getEjemplar());
            redirectAttributes.addFlashAttribute("success", "Préstamo creado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear préstamo: " + e.getMessage());
        }
        return "redirect:/prestamos";
    }

    @PostMapping("/devolver/{id}")
    public String devolverPrestamo(@PathVariable Long id, RedirectAttributes ra) {
        try {
            prestamoService.devolver(id);
            ra.addFlashAttribute("success", "Préstamo devuelto correctamente");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al devolver préstamo: " + e.getMessage());
        }
        return "redirect:/prestamos";
    }

    @GetMapping("/mis-prestamos")
    public String misPrestamos(@AuthenticationPrincipal User user, Model model) {
        Usuario usuario = usuarioService.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<Prestamo> prestamos = prestamoService.findByUsuario(usuario);
        model.addAttribute("prestamos", prestamos);
        return "prestamos/mis-libros";
    }

    private void cargarDatosFormulario(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        List<Ejemplar> ejemplares = ejemplarService.findByPrestadoFalse();

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("ejemplares", ejemplares);
    }
}
package com.example.proyecto.controller;
import com.example.proyecto.model.Usuario;
import com.example.proyecto.model.*;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.proyecto.service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.ModelAttribute;





@Controller
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute Usuario usuario,
                                    BindingResult br,
                                    Model model,
                                    RedirectAttributes ra) {
        if (br.hasErrors()) {
            return "registro";
        }

        if (usuarioService.existeUsername(usuario.getUsername())) {
            model.addAttribute("error", "El nombre de usuario ya está en uso.");
            return "registro";
        }

        usuario.setRol(Rol.USER); // Todos los registros nuevos serán USER
        usuarioService.crearUsuario(usuario); // Se asegura de encriptar la contraseña
        ra.addFlashAttribute("ok", "Cuenta creada correctamente. Ahora puedes iniciar sesión.");
        return "redirect:/login";
    }
}

package org.example.blogdam.controllers;

import org.example.blogdam.entities.Comentario;
import org.example.blogdam.entities.Noticia;
import org.example.blogdam.repositories.RepositorioComentarios;
import org.example.blogdam.repositories.RepositorioNoticias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class ControladorPrincipal {
    @Autowired
    private RepositorioNoticias repositorioNoticias;

    @Autowired
    private RepositorioComentarios repositorioComentarios;

    //AÑadidos cambios para ver la lista de comentarios
    @GetMapping("/")
    public String index(Model model) {
        List<Noticia> listaNoticias = repositorioNoticias.findAll();
        List<Comentario> listaComentarios = repositorioComentarios.findTop5ByOrderByFechaDesc();
        model.addAttribute("comentarios", listaComentarios);
        model.addAttribute("lista", listaNoticias);
        return "index";
    }

    @GetMapping("/verNoticia/{id}")
    public String verNoticia(Model model, @PathVariable Long id) {
        Noticia noticia = repositorioNoticias.findById(id).orElse(null);
        model.addAttribute("noticia", noticia);
        Comentario comentario = new Comentario();
        comentario.setFecha(Date.valueOf(LocalDate.now()));
        comentario.setNoticia(noticia);
        model.addAttribute("comentario", comentario);
        model.addAttribute("comentarios", repositorioComentarios.findByNoticia(noticia));
        return "verNoticia";
    }

    @PostMapping("/comentar")
    public String comentar(@ModelAttribute Comentario comentario, Model model) {
        System.out.println(comentario.getTexto());
        repositorioComentarios.save(comentario);
        return "redirect:/verNoticia/" + comentario.getNoticia().getId();
    }

    @PostMapping("/cambiarEstado")
    public String cambiarEstado(@RequestParam Long id, @ModelAttribute Comentario comentario, Model model) {
        try {
            // Buscar el comentario por id
            Optional<Comentario> comentarioOpt = repositorioComentarios.findById(id);
            if (comentarioOpt.isPresent()) {
                Comentario comentarioEncontrado = comentarioOpt.get();
                //Cambiamos el estado del comentario segun si es true o false, de true a false y viciversa
                comentarioEncontrado.setValidado(!comentarioEncontrado.isValidado());
                repositorioComentarios.save(comentarioEncontrado);
                return "redirect:/verNoticia/" + comentarioEncontrado.getNoticia().getId();
            }
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/";
        }
    }



}

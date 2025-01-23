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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        // Crear mapa para contar los comentarios por noticia
        Map<Long, Long> contadorComentarios = new HashMap<>();
        for (Noticia noticia : listaNoticias) {
            long contador = repositorioComentarios.countByNoticia(noticia);
            contadorComentarios.put(noticia.getId(), contador);
        }

        // Añadir datos al modelo
        model.addAttribute("comentarios", listaComentarios);
        model.addAttribute("lista", listaNoticias);
        model.addAttribute("contadorComentarios", contadorComentarios); // <--- Aquí lo añadimos

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





}

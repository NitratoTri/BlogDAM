package org.example.blogdam.controllers;

import org.example.blogdam.entities.Categoria;
import org.example.blogdam.entities.Comentario;
import org.example.blogdam.entities.Noticia;
import org.example.blogdam.entities.TipoCategoria;
import org.example.blogdam.repositories.RepositorioCategorias;
import org.example.blogdam.repositories.RepositorioComentarios;
import org.example.blogdam.repositories.RepositorioNoticias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.blogdam.servicios.FileProcessingService;

import javax.swing.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Controller
public class ControladorCrud {
    @Autowired
    private RepositorioNoticias repo;
    @Autowired
    private FileProcessingService service;
    @Autowired
    private RepositorioComentarios repoComentarios;
    @Autowired
    private RepositorioCategorias repoCategorias;
    @GetMapping("/crud/noticias")
    public String lista(Model model) {
        List<Noticia> listaNoticias = repo.findAll();
        model.addAttribute("lista", listaNoticias);
        return "listaNoticias";
    }

    @GetMapping("/crud/noticias/insertar")
    public String muestraFormulario(Model model) {
        Noticia noticia = new Noticia();
        noticia.setFecha(Date.valueOf(LocalDate.now()));

        // Envía las categorías desde la base de datos
        List<Categoria> categorias = repoCategorias.findAll();

        // Envía los valores del enum TipoCategoria
        model.addAttribute("tiposCategorias", TipoCategoria.values());

        model.addAttribute("categorias", categorias);
        model.addAttribute("title", "Insertar Noticia");
        model.addAttribute("noticia", noticia);
        return "formularioNoticias";
    }

    @PostMapping("/crud/noticias/insertar")
    public String insertar(@ModelAttribute Noticia noticia, @RequestParam("fichero") MultipartFile fichero) {
        // Recuperar la categoría seleccionada
        Optional<Categoria> categoriaOpt = repoCategorias.findById(noticia.getCategoria().getId());
        if (categoriaOpt.isPresent()) {
            noticia.setCategoria(categoriaOpt.get());
        } else {
            // Manejar el caso en que la categoría no existe
            return "redirect:/crud/noticias/insertar?error=CategoriaInvalida";
        }

        // Guardar la noticia
        noticia = repo.save(noticia);

        // Manejo de imágenes
        if (!fichero.isEmpty()) {
            String nombreOriginal = fichero.getOriginalFilename();
            String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf(".") + 1);
            String img = "n-" + noticia.getId() + "." + extension;
            service.uploadFile(fichero, img);
            noticia.setImagen(img);
            repo.save(noticia);
        }

        return "redirect:/";
    }



    @GetMapping("/crud/noticias/modificar/{id}")
    public String modificar(Model model, @PathVariable Long id) {
        String url = "redirect:/crud/noticias";
        model.addAttribute("title", "Modificar Noticia");
        Optional<Noticia> noticia = repo.findById(id);
        if (noticia.isPresent()) {
            model.addAttribute("noticia", noticia.get());
            return "formularioNoticias";
        }
        return url;
    }

    @GetMapping("/crud/noticias/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/crud/noticias";
    }

    @PostMapping("/cambiarEstado")
    public String cambiarEstado(@RequestParam Long id, @ModelAttribute Comentario comentario, Model model) {
        try {
            // Buscar el comentario por id
            Optional<Comentario> comentarioOpt = repoComentarios.findById(id);
            if (comentarioOpt.isPresent()) {
                Comentario comentarioEncontrado = comentarioOpt.get();
                //Cambiamos el estado del comentario segun si es true o false, de true a false y viciversa
                comentarioEncontrado.setValidado(!comentarioEncontrado.isValidado());
                repoComentarios.save(comentarioEncontrado);
                return "redirect:/verNoticia/" + comentarioEncontrado.getNoticia().getId();
            }
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/";
        }
    }
}


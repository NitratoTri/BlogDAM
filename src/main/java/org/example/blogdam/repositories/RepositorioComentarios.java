package org.example.blogdam.repositories;

import org.example.blogdam.entities.Comentario;
import org.example.blogdam.entities.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioComentarios extends JpaRepository<Comentario,Long> {
    public List<Comentario> findByNoticia(Noticia noticia);
    public List<Comentario> findTop5ByOrderByFechaDesc();
}

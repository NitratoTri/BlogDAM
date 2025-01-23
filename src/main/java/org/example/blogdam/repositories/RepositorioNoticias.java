package org.example.blogdam.repositories;

import org.example.blogdam.entities.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioNoticias extends JpaRepository<Noticia, Long> {
}

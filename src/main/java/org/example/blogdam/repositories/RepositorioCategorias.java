package org.example.blogdam.repositories;

import org.example.blogdam.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioCategorias extends JpaRepository<Categoria, Long> {
}

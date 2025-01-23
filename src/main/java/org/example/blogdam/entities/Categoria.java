package org.example.blogdam.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Entity
@Data
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String tipo;
    //Relación many-to-one con la entidad Noticia para relacionar categorías con noticias
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Noticia> noticias;
}
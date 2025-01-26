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

    //Enumerado para clasificar las categorias en tipos
    //EnumType.STRING para guardar el nombre del enum en la base de datos
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TipoCategoria tipo;

    //Relación many-to-one con la entidad Noticia para relacionar categorías con noticias
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Noticia> noticias;
}
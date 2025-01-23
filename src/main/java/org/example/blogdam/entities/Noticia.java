package org.example.blogdam.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class Noticia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String titulo;
    @Column(columnDefinition="text")
    private String contenido;

    private String imagen;
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "categoria_id") // Nombre de la columna en la tabla Noticia
    private Categoria categoria;
}

package org.example.blogdam.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class Comentario {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String autor;
    @Column(columnDefinition = "TEXT")
    private String texto;
    private Date fecha;
    //Propiedad boolean de un comentario que indica si ha sido validado por el administrador
    private boolean validado;
    @ManyToOne
    private Noticia noticia;
}

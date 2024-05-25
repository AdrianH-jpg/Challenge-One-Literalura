package com.alura.literalura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.stream.Collectors;

@Entity
@Table(
        name = "libros"
)
public class Libro {
    @Id
    private Long id;
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private String copyright;
    private Integer descargas;
    @ManyToOne
    private Autor autor;

    public Libro() {
    }

    public Libro(DatosLibro libro) {
        this.id = libro.id();
        this.titulo = libro.titulo();
        this.idioma = Idioma.fromString((String)libro.idiomas().stream().limit(1L).collect(Collectors.joining()));
        this.copyright = libro.copyright();
        this.descargas = libro.descargas();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdioma() {
        return this.idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public String getCopyright() {
        return this.copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Integer getDescargas() {
        return this.descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public Autor getAutor() {
        return this.autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String toString() {
        return "\n------------ Libro ------------\nid=" + this.id + ", titulo='" + this.titulo + "', idioma=" + this.idioma + ", copyright='" + this.copyright + "', descargas=" + this.descargas + ", autor=" + this.autor + "\n-----------------------------------\n";
    }
}

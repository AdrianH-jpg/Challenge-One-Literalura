package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public record DatosLibro(Long id, String titulo, List<DatosAutor> autores, List<String> idiomas, String copyright, Integer descargas) {
    public DatosLibro(@JsonAlias({"id"}) Long id, @JsonAlias({"title"}) String titulo, @JsonAlias({"authors"}) List<DatosAutor> autores, @JsonAlias({"languages"}) List<String> idiomas, @JsonAlias({"copyright"}) String copyright, @JsonAlias({"download_count"}) Integer descargas) {
        this.id = id;
        this.titulo = titulo;
        this.autores = autores;
        this.idiomas = idiomas;
        this.copyright = copyright;
        this.descargas = descargas;
    }

    @JsonAlias({"id"})
    public Long id() {
        return this.id;
    }

    @JsonAlias({"title"})
    public String titulo() {
        return this.titulo;
    }

    @JsonAlias({"authors"})
    public List<DatosAutor> autores() {
        return this.autores;
    }

    @JsonAlias({"languages"})
    public List<String> idiomas() {
        return this.idiomas;
    }

    @JsonAlias({"copyright"})
    public String copyright() {
        return this.copyright;
    }

    @JsonAlias({"download_count"})
    public Integer descargas() {
        return this.descargas;
    }
}

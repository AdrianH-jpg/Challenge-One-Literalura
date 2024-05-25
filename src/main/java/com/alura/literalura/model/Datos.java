package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public record Datos(Integer total, List<DatosLibro> libros) {
    public Datos(@JsonAlias({"count"}) Integer total, @JsonAlias({"results"}) List<DatosLibro> libros) {
        this.total = total;
        this.libros = libros;
    }

    @JsonAlias({"count"})
    public Integer total() {
        return this.total;
    }

    @JsonAlias({"results"})
    public List<DatosLibro> libros() {
        return this.libros;
    }
}

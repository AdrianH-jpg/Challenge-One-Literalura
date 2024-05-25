package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public record DatosAutor(String nombre, Integer nacimiento, Integer fallecimiento) {
    public DatosAutor(@JsonAlias({"name"}) String nombre, @JsonAlias({"birth_year"}) Integer nacimiento, @JsonAlias({"death_year"}) Integer fallecimiento) {
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.fallecimiento = fallecimiento;
    }

    @JsonAlias({"name"})
    public String nombre() {
        return this.nombre;
    }

    @JsonAlias({"birth_year"})
    public Integer nacimiento() {
        return this.nacimiento;
    }

    @JsonAlias({"death_year"})
    public Integer fallecimiento() {
        return this.fallecimiento;
    }
}


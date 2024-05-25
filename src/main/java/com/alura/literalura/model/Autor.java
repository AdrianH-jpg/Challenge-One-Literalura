package com.alura.literalura.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(
        name = "autores"
)
public class Autor {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(
            unique = true
    )
    private String nombre;
    private Integer nacimiento;
    private Integer fallecimiento;
    @OneToMany(
            mappedBy = "autor",
            cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER
    )
    private List<Libro> libros;

    public Autor() {
    }

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.nacimiento = datosAutor.nacimiento();
        this.fallecimiento = datosAutor.fallecimiento();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNacimiento() {
        return this.nacimiento;
    }

    public void setNacimiento(Integer nacimiento) {
        this.nacimiento = nacimiento;
    }

    public Integer getFallecimiento() {
        return this.fallecimiento;
    }

    public void setFallecimiento(Integer fallecimiento) {
        this.fallecimiento = fallecimiento;
    }

    public List<Libro> getLibros() {
        return this.libros;
    }

    public void setLibros(List<Libro> libros) {
        libros.forEach((l) -> {
            l.setAutor(this);
        });
        this.libros = libros;
    }

    public String toString() {
        return "\n------------ Autor ------------\nid=" + this.id + "Nombre='" + this.nombre + "\nAño de nacimiento=" + this.nacimiento + "Año de fallecimiento=" + this.fallecimiento + "\n-----------------------------------\n";
    }
}
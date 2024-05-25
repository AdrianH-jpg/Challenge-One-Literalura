package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Datos;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import java.io.PrintStream;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado;
    private ConsumoAPI consumoAPI;
    private ConvierteDatos conversor;
    private String URL_BASE;
    private AutorRepository repository;

    public Principal(AutorRepository repository) {
        this.teclado = new Scanner(System.in);
        this.consumoAPI = new ConsumoAPI();
        this.conversor = new ConvierteDatos();
        this.URL_BASE = "https://gutendex.com/books/";
        this.repository = repository;
    }

    public void mostrarMenu() {
        int opcion = -1;
        String menu = "----- \ud83d\udcda Bienvenido(a) a Literalura \ud83d\udcda -----\n--------------------------------------------\n             \ud83d\udcd1 MENU PRINCIPAL \ud83d\udcd1\n--------------------------------------------\n1 - Buscar Libros por TÍtulo\n2 - Buscar Autor por Nombre\n3 - Listar Libros Registrados\n4 - Listar Autores Registrados\n5 - Listar Autores Vivos\n6 - Listar Libros por Idioma\n7 - Listar Autores por Año\n8 - Top 10 Libros más Buscados\n9 - Generar Estadísticas\n----------------------------------------------\n0 - \ud83c\udd97 SALIR DEL PROGRAMA \ud83c\udd97\n----------------------------------------------\nElija una opción:\n";

        while(opcion != 0) {
            System.out.println(menu);

            try {
                opcion = Integer.valueOf(this.teclado.nextLine());
                switch (opcion) {
                    case 0:
                        System.out.println("Gracias por utilizar Literalura ✔️");
                        System.out.println("Cerrando la aplicacion Literalura \ud83d\udcd3 ...");
                        break;
                    case 1:
                        this.buscarLibroPorTitulo();
                        break;
                    case 2:
                        this.buscarAutorPorNombre();
                        break;
                    case 3:
                        this.listarLibrosRegistrados();
                        break;
                    case 4:
                        this.listarAutoresRegistrados();
                        break;
                    case 5:
                        this.listarAutoresVivos();
                        break;
                    case 6:
                        this.listarLibrosPorIdioma();
                        break;
                    case 7:
                        this.listarAutoresPorAnio();
                        break;
                    case 8:
                        this.top10Libros();
                        break;
                    case 9:
                        this.generarEstadisticas();
                        break;
                    default:
                        System.out.println("Opción no válida!");
                }
            } catch (NumberFormatException var4) {
                NumberFormatException e = var4;
                System.out.println("Opción no válida: " + e.getMessage());
            }
        }

    }

    public void buscarLibroPorTitulo() {
        System.out.println("--------------------------------\n \ud83d\udcd4 BUSCAR LIBROS POR TÍTULO \ud83d\udcd4\n--------------------------------\n");
        System.out.println("Introduzca el nombre del libro que desea buscar:");
        String nombre = this.teclado.nextLine();
        String var10001 = this.URL_BASE;
        String json = this.consumoAPI.obtenerDatos(var10001 + "?search=" + nombre.replace(" ", "+").toLowerCase());
        if (json.isEmpty() || !json.contains("\"count\":0,\"next\":null,\"previous\":null,\"results\":[]")) {
            Datos datos = (Datos)this.conversor.obtenerDatos(json, Datos.class);
            Optional<DatosLibro> libroBuscado = datos.libros().stream().findFirst();
            if (libroBuscado.isPresent()) {
                PrintStream var10000 = System.out;
                var10001 = ((DatosLibro)libroBuscado.get()).titulo();
                var10000.println("\n------------- LIBRO \ud83d\udcd9  --------------\nTítulo: " + var10001 + "\nAutor: " + (String)((DatosLibro)libroBuscado.get()).autores().stream().map((a) -> {
                    return a.nombre();
                }).limit(1L).collect(Collectors.joining()) + "\nIdioma: " + (String)((DatosLibro)libroBuscado.get()).idiomas().stream().collect(Collectors.joining()) + "\nNúmero de descargas: " + ((DatosLibro)libroBuscado.get()).descargas() + "\n--------------------------------------\n");

                try {
                    List<Libro> libroEncontrado = (List)libroBuscado.stream().map((a) -> {
                        return new Libro(a);
                    }).collect(Collectors.toList());
                    Autor autorAPI = (Autor)((List)libroBuscado.stream().flatMap((l) -> {
                        return l.autores().stream().map((a) -> {
                            return new Autor(a);
                        });
                    }).collect(Collectors.toList())).stream().findFirst().get();
                    Optional<Autor> autorBD = this.repository.buscarAutorPorNombre((String)((DatosLibro)libroBuscado.get()).autores().stream().map((a) -> {
                        return a.nombre();
                    }).collect(Collectors.joining()));
                    Optional<Libro> libroOptional = this.repository.buscarLibroPorNombre(nombre);
                    if (libroOptional.isPresent()) {
                        System.out.println("El libro ya está guardado en la BD.");
                    } else {
                        Autor autor;
                        if (autorBD.isPresent()) {
                            autor = (Autor)autorBD.get();
                            System.out.println("EL autor ya esta guardado en la BD");
                        } else {
                            autor = autorAPI;
                            this.repository.save(autor);
                        }

                        autor.setLibros(libroEncontrado);
                        this.repository.save(autor);
                    }
                } catch (Exception var10) {
                    Exception e = var10;
                    System.out.println("Warning! " + e.getMessage());
                }
            } else {
                System.out.println("Libro no encontrado!");
            }
        }

    }

    public void buscarAutorPorNombre() {
        System.out.println("-------------------------------\n \ud83d\udcd9 BUSCAR AUTOR POR NOMBRE \ud83d\udcd9\n-------------------------------\n");
        System.out.println("Ingrese el nombre del autor que deseas buscar:");
        String nombre = this.teclado.nextLine();
        Optional<Autor> autor = this.repository.buscarAutorPorNombre(nombre);
        if (autor.isPresent()) {
            PrintStream var10000 = System.out;
            String var10001 = ((Autor)autor.get()).getNombre();
            var10000.println("\nAutor: " + var10001 + "\nFecha de Nacimiento: " + ((Autor)autor.get()).getNacimiento() + "\nFecha de Fallecimiento: " + ((Autor)autor.get()).getFallecimiento() + "\nLibros: " + ((Autor)autor.get()).getLibros().stream().map((l) -> {
                return l.getTitulo();
            }).collect(Collectors.toList()) + "\n");
        } else {
            System.out.println("El autor no existe en la BD");
        }

    }

    public void listarLibrosRegistrados() {
        System.out.println("----------------------------------\n \ud83d\udcd5 LISTAR LIBROS REGISTRADOS \ud83d\udcd5\n----------------------------------\n");
        List<Libro> libros = this.repository.buscarTodosLosLibros();
        libros.forEach((l) -> {
            PrintStream var10000 = System.out;
            String var10001 = l.getTitulo();
            var10000.println("-------------- LIBRO \ud83d\udcd9  -----------------\nTítulo: " + var10001 + "\nAutor: " + l.getAutor().getNombre() + "\nIdioma: " + l.getIdioma().getIdioma() + "\nNúmero de descargas: " + l.getDescargas() + "\n----------------------------------------\n");
        });
    }

    public void listarAutoresRegistrados() {
        System.out.println("----------------------------------\n \ud83d\udcd7 LISTAR AUTORES REGISTRADOS \ud83d\udcd7\n----------------------------------\n");
        List<Autor> autores = this.repository.findAll();
        System.out.println();
        autores.forEach((l) -> {
            PrintStream var10000 = System.out;
            String var10001 = l.getNombre();
            var10000.println("Autor: " + var10001 + "\nFecha de Nacimiento: " + l.getNacimiento() + "\nFecha de Fallecimiento: " + l.getFallecimiento() + "\nLibros: " + l.getLibros().stream().map((t) -> {
                return t.getTitulo();
            }).collect(Collectors.toList()) + "\n");
        });
    }

    public void listarAutoresVivos() {
        System.out.println("-----------------------------\n  \ud83d\udcd2 LISTAR AUTORES VIVOS \ud83d\udcd2\n-----------------------------\n");
        System.out.println("Introduzca un año para verificar el autor(es) que desea buscar:");

        try {
            Integer fecha = Integer.valueOf(this.teclado.nextLine());
            List<Autor> autores = this.repository.buscarAutoresVivos(fecha);
            if (!autores.isEmpty()) {
                System.out.println();
                autores.forEach((a) -> {
                    PrintStream var10000 = System.out;
                    String var10001 = a.getNombre();
                    var10000.println("Autor: " + var10001 + "\nFecha de Nacimiento: " + a.getNacimiento() + "\nFecha de Fallecimiento: " + a.getFallecimiento() + "\nLibros: " + a.getLibros().stream().map((l) -> {
                        return l.getTitulo();
                    }).collect(Collectors.toList()) + "\n");
                });
            } else {
                System.out.println("No hay autores vivos en el año registrado");
            }
        } catch (NumberFormatException var3) {
            NumberFormatException e = var3;
            System.out.println("Ingresa un año válido " + e.getMessage());
        }

    }

    public void listarLibrosPorIdioma() {
        System.out.println("--------------------------------\n \ud83d\udcd8 LISTAR LIBROS POR IDIOMA \ud83d\udcd8\n--------------------------------\n");
        String menu = "---------------------------------------------------\nSeleccione el idioma del libro que desea encontrar:\n---------------------------------------------------\n1 - Español\n2 - Francés\n3 - Inglés\n4 - Portugués\n----------------------------------------------------\n";
        System.out.println(menu);

        try {
            int opcion = Integer.parseInt(this.teclado.nextLine());
            switch (opcion) {
                case 1 -> this.buscarLibrosPorIdioma("es");
                case 2 -> this.buscarLibrosPorIdioma("fr");
                case 3 -> this.buscarLibrosPorIdioma("en");
                case 4 -> this.buscarLibrosPorIdioma("pt");
                default -> System.out.println("Opción inválida!");
            }
        } catch (NumberFormatException var3) {
            NumberFormatException e = var3;
            System.out.println("Opción no válida: " + e.getMessage());
        }

    }

    private void buscarLibrosPorIdioma(String idioma) {
        try {
            Idioma idiomaEnum = Idioma.valueOf(idioma.toUpperCase());
            List<Libro> libros = this.repository.buscarLibrosPorIdioma(idiomaEnum);
            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados en ese idioma");
            } else {
                System.out.println();
                libros.forEach((l) -> {
                    PrintStream var10000 = System.out;
                    String var10001 = l.getTitulo();
                    var10000.println("----------- LIBRO \ud83d\udcd9  --------------\nTítulo: " + var10001 + "\nAutor: " + l.getAutor().getNombre() + "\nIdioma: " + l.getIdioma().getIdioma() + "\nNúmero de descargas: " + l.getDescargas() + "\n----------------------------------------\n");
                });
            }
        } catch (IllegalArgumentException var4) {
            System.out.println("Introduce un idioma válido en el formato especificado.");
        }

    }

    public void listarAutoresPorAnio() {
        System.out.println("------------------------------\n \ud83d\udcd3 LISTAR AUTORES POR AÑO \ud83d\udcd3\n------------------------------\n");
        String menu = "------------------------------------------\nIngresa una opción para listar los autores\n-------------------------------------------\n1 - Listar autor por Año de Nacimiento\n2 - Listar autor por año de Fallecimiento\n-------------------------------------------\n";
        System.out.println(menu);

        try {
            Integer opcion = Integer.valueOf(this.teclado.nextLine());
            switch (opcion) {
                case 1 -> this.ListarAutoresPorNacimiento();
                case 2 -> this.ListarAutoresPorFallecimiento();
                default -> System.out.println("Opción inválida!");
            }
        } catch (NumberFormatException var3) {
            NumberFormatException e = var3;
            System.out.println("Opción no válida: " + e.getMessage());
        }

    }

    public void ListarAutoresPorNacimiento() {
        System.out.println("---------------------------------------------\n \ud83d\udcd6 BUSCAR AUTOR POR SU AÑO DE NACIMIENTO \ud83d\udcd6\n---------------------------------------------\n");
        System.out.println("Introduzca el año de nacimiento del autor que desea buscar:");

        try {
            Integer nacimiento = Integer.valueOf(this.teclado.nextLine());
            List<Autor> autores = this.repository.listarAutoresPorNacimiento(nacimiento);
            if (autores.isEmpty()) {
                System.out.println("No existen autores con año de nacimiento igual a " + nacimiento);
            } else {
                System.out.println();
                autores.forEach((a) -> {
                    PrintStream var10000 = System.out;
                    String var10001 = a.getNombre();
                    var10000.println("Autor: " + var10001 + "\nFecha de Nacimiento: " + a.getNacimiento() + "\nFecha de Fallecimiento: " + a.getFallecimiento() + "\nLibros: " + a.getLibros().stream().map((l) -> {
                        return l.getTitulo();
                    }).collect(Collectors.toList()) + "\n");
                });
            }
        } catch (NumberFormatException var3) {
            NumberFormatException e = var3;
            System.out.println("Año no válido: " + e.getMessage());
        }

    }

    public void ListarAutoresPorFallecimiento() {
        System.out.println("---------------------------------------------------------\n \ud83d\udcd6  BUSCAR LIBROS POR AÑO DE FALLECIMIENTO DEL AUTOR \ud83d\udcd6\n----------------------------------------------------------\n");
        System.out.println("Introduzca el año de fallecimiento del autor que desea buscar:");

        try {
            Integer fallecimiento = Integer.valueOf(this.teclado.nextLine());
            List<Autor> autores = this.repository.listarAutoresPorFallecimiento(fallecimiento);
            if (autores.isEmpty()) {
                System.out.println("No existen autores con año de fallecimiento igual a " + fallecimiento);
            } else {
                System.out.println();
                autores.forEach((a) -> {
                    PrintStream var10000 = System.out;
                    String var10001 = a.getNombre();
                    var10000.println("Autor: " + var10001 + "\nFecha de Nacimiento: " + a.getNacimiento() + "\nFecha de Fallecimeinto: " + a.getFallecimiento() + "\nLibros: " + a.getLibros().stream().map((l) -> {
                        return l.getTitulo();
                    }).collect(Collectors.toList()) + "\n");
                });
            }
        } catch (NumberFormatException var3) {
            NumberFormatException e = var3;
            System.out.println("Opción no válida: " + e.getMessage());
        }

    }

    public void top10Libros() {
        System.out.println("-------------------------------------\n   \ud83d\udcda TOP 10 LIBROS MÁS BUSCADOS \ud83d\udcda\n-------------------------------------\n");
        List<Libro> libros = this.repository.top10Libros();
        System.out.println();
        libros.forEach((l) -> {
            PrintStream var10000 = System.out;
            String var10001 = l.getTitulo();
            var10000.println("----------------- LIBRO \ud83d\udcd9  ----------------\nTítulo: " + var10001 + "\nAutor: " + l.getAutor().getNombre() + "\nIdioma: " + l.getIdioma().getIdioma() + "\nNúmero de descargas: " + l.getDescargas() + "\n-------------------------------------------\n");
        });
    }

    public void generarEstadisticas() {
        System.out.println("----------------------------\n \ud83d\udcca GENERAR ESTADÍSTICAS \ud83d\udcca\n----------------------------\n");
        String json = this.consumoAPI.obtenerDatos(this.URL_BASE);
        Datos datos = (Datos)this.conversor.obtenerDatos(json, Datos.class);
        IntSummaryStatistics est = (IntSummaryStatistics)datos.libros().stream().filter((l) -> {
            return l.descargas() > 0;
        }).collect(Collectors.summarizingInt(DatosLibro::descargas));
        Integer media = (int)est.getAverage();
        System.out.println("\n--------- ESTADÍSTICAS \ud83d\udcca ------------");
        System.out.println("Media de descargas: " + media);
        System.out.println("Máxima de descargas: " + est.getMax());
        System.out.println("Mínima de descargas: " + est.getMin());
        System.out.println("Total registros para calcular las estadísticas: " + est.getCount());
        System.out.println("---------------------------------------------------\n");
    }
}

package com.alura.literalura.model;

public enum Idioma {
    ES("es"),
    FR("fr"),
    EN("en"),
    PT("pt");

    private String idioma;

    private Idioma(String idioma) {
        this.idioma = idioma;
    }

    public String getIdioma() {
        return this.idioma;
    }

    public static Idioma fromString(String text) {
        Idioma[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Idioma idioma = var1[var3];
            if (idioma.idioma.equalsIgnoreCase(text)) {
                return idioma;
            }
        }

        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}

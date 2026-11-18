package com.example.verificota.enumeration;

import java.util.Arrays;
import java.util.Optional;

public enum Genero {
    HOMBRE(1),
    MUJER(2);

    private int idGenero;

    Genero(int idGenero) {
        this.idGenero = idGenero;
    }

    public static boolean esValido(Integer idGenero) {
        return desdeId(idGenero).isPresent();
    }

    private static Optional<Genero> desdeId(Integer idGenero) {
        if(idGenero == null) {
            return Optional.empty();
        }

        return Arrays.stream(values())
                .filter(item -> item.idGenero == idGenero)
                .findFirst();
    }
}

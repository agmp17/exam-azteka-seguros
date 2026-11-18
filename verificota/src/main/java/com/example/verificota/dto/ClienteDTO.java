package com.example.verificota.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ClienteDTO(
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaNacimiento,

        Integer idGenero
) {}

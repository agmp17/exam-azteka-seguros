package com.example.consultita.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeguroDTO {

    private Integer id;
    private String nombre;
    private String descripcion;
    private String primaTotal;

    private List<String> beneficios;
    private RequisitoDTO requisitos;

}

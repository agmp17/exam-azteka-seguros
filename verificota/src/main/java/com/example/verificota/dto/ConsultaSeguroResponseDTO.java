package com.example.verificota.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaSeguroResponseDTO {

    private List<SeguroDTO> seguros;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeguroDTO {

        private Integer id;
        private String nombre;
        private String descripcion;
        private String primaTotal;
        private List<String> beneficios;
        private RequisitosDTO requisitos;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequisitosDTO {

        private Integer edadMinima;
        private Integer edadMaxima;
        private List<Integer> idGeneros;
    }
}
package com.example.consultita.dtobis;

import java.util.List;

public record SeguroBisDTO(
        Integer id,
        String nombre,
        String descripcion,
        String primaTotal,
        List<BeneficioBisDTO> beneficios,
        RequisitoBisDTO requisitos) {}

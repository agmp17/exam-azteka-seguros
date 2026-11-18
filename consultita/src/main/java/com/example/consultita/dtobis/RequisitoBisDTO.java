package com.example.consultita.dtobis;

import java.util.List;

public record RequisitoBisDTO(
        Integer edadMinima,
        Integer edadMaxima,
        List<GeneroBisDTO> genero
) {}

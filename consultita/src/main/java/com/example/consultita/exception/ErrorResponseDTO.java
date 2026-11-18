package com.example.consultita.exception;

public record ErrorResponseDTO(
        int codigo,
        String mensaje
) {}

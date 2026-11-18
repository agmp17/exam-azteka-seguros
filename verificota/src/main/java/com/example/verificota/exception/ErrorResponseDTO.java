package com.example.verificota.exception;

public record ErrorResponseDTO(
        int codigo,
        String mensaje
) {}

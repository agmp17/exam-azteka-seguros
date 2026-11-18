package com.example.verificota.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ManejadorGlogalDeExcepciones extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ManejadorGlogalDeExcepciones.class);
    private static final String ID_INVALIDO = "El IDENT ingresado no es válido. "
            + "Por favor verifica y vuelve a intentarlo.";

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String mensaje;

        if (status.is5xxServerError()) {
            log.error("Error interno al atender la solicitud", ex);
            mensaje = "Ocurrió un error interno. Por favor intenta nuevamente más tarde.";

        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            // Por ejemplo: GET /api/seguros/abc
            mensaje = ID_INVALIDO;

        } else if (ex instanceof ResponseStatusException responseException && responseException.getReason() != null) {
            // Mensajes controlados que definimos en SeguroService.
            mensaje = responseException.getReason();

        } else {
            mensaje = mensajePorEstado(status.value());
        }

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(status.value(), mensaje);

        return super.handleExceptionInternal(
                ex,
                errorResponseDTO,
                headers,
                status,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> manejarErrorInesperado(Exception ex) {
        // El detalle técnico queda en los logs del servidor.
        log.error("Error inesperado al procesar la solicitud", ex);

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrió un error interno. Por favor intenta nuevamente más tarde."
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponseDTO);
    }

    private String mensajePorEstado(int codigo) {
        return switch (codigo) {
            case 400 -> "La solicitud contiene datos inválidos.";
            case 404 -> "No se encontró el recurso solicitado.";
            case 405 -> "El método HTTP no está permitido para este recurso.";
            case 406 -> "El formato de respuesta solicitado no está disponible.";
            case 415 -> "El tipo de contenido enviado no está soportado.";
            default -> "No fue posible procesar la solicitud.";
        };
    }
}

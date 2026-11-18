package com.example.verificota.service;

import com.example.verificota.dto.ClienteDTO;
import com.example.verificota.dto.ConsultaSeguroResponseDTO;
import com.example.verificota.dto.ConsultaSeguroResponseDTO.SeguroDTO;
import com.example.verificota.dto.ValidaRequestDTO;
import com.example.verificota.dto.ValidaResponseDTO;
import com.example.verificota.cliente.IConsultaSeguroCliente;

import com.example.verificota.enumeration.Genero;
import feign.FeignException;
import feign.RetryableException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

@Service
public class ValidarService {

    private final IConsultaSeguroCliente consultaSeguroCliente;

    public ValidarService(IConsultaSeguroCliente consultaSeguroCliente) {
        this.consultaSeguroCliente = consultaSeguroCliente;
    }

    // Consulta el microserv (consultita) Valida con el endpoint que consulta la BD
    public ResponseEntity<ValidaResponseDTO> validar(ValidaRequestDTO validaRequestDTO) {
        return validarConOrigen(validaRequestDTO, false);
    }

    // Consulta el microserv (consultita) Valida con el endpoint que consulta el JSON
    public ResponseEntity<ValidaResponseDTO> validarJson(ValidaRequestDTO ValidaRequestDTO) {
        return validarConOrigen(ValidaRequestDTO, true);
    }

    private ResponseEntity<ValidaResponseDTO> validarConOrigen(ValidaRequestDTO validaRequestDTO, boolean desdeJson) {

        LocalDate hoy = LocalDate.now(ZoneId.of("America/Mexico_City"));
        validarReq(validaRequestDTO, hoy);

        SeguroDTO seguroConsultita = obtenerSeguroConsultita(validaRequestDTO.idSeguro(), desdeJson);
        ClienteDTO clienteReq = validaRequestDTO.cliente();

        // Se validan los datos que llegan por POST a verificota VS los datos que se consulta a la api consultita
        var requisitosSeguro = seguroConsultita.getRequisitos();
        int edad = Period.between(clienteReq.fechaNacimiento(), hoy).getYears();
        boolean cumpleEdad = edad >= requisitosSeguro.getEdadMinima() && edad <= requisitosSeguro.getEdadMaxima();
        boolean cumpleGenero = requisitosSeguro.getIdGeneros().contains(clienteReq.idGenero());

        HttpStatus codigo = HttpStatus.CREATED;
        String msg = "Nos complace informarle que cumple con toditos los requisitos "
                     + "para acceder al seguro, " + seguroConsultita.getNombre() + " ¡FeELICIDADES!";

        if(!cumpleEdad || !cumpleGenero) {
            codigo = HttpStatus.UNAUTHORIZED;
            msg = "Lamentamos informarle que no podemos ofrecerle el seguro "
                    + seguroConsultita.getNombre() + " porque no cumple con ciertos requisitos establecidos. "
                    + "Revise los criterios necesarios o considere otra opción";
        }

        return ResponseEntity.status(codigo).body(new ValidaResponseDTO(codigo.value(), msg));
    }

    private void validarReq(ValidaRequestDTO validaRequestDTO, LocalDate hoy) {

        if(validaRequestDTO == null || validaRequestDTO.idSeguro() == null || validaRequestDTO.idSeguro() <=0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe proporcionar un identificador de seguro válido");
        }

        ClienteDTO clienteDTO = validaRequestDTO.cliente();

        if(clienteDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe proporcionar un cliente");
        }

        if((clienteDTO.nombre() == null || clienteDTO.nombre().isEmpty()) ||
           (clienteDTO.apellidoPaterno() == null || clienteDTO.apellidoMaterno().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre y el apellido paterno son obligatorios");
        }

        if(clienteDTO.fechaNacimiento() == null || clienteDTO.fechaNacimiento().isAfter((hoy))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de nacimiento no puede ser posterior a la fecha actual");
        }

        if(!Genero.esValido(clienteDTO.idGenero())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El género debe ser 1 (hombre) o 2 (mujer)");
        }

        if(clienteDTO.idGenero() == null || (clienteDTO.idGenero() != 1 && clienteDTO.idGenero() != 2)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El género debe ser 1 (hombre) o 2 (mujer)");
        }
    }

    private SeguroDTO obtenerSeguroConsultita(Integer idSeguro, boolean desdeJson) {

        ConsultaSeguroResponseDTO consultaSeguroResponseDTO;

        try {
            // Se hace la consulta del seguro al otro microservicio
            consultaSeguroResponseDTO = (desdeJson) ? consultaSeguroCliente.consultarJsonPorId(idSeguro)
                                                    : consultaSeguroCliente.consultarPorId(idSeguro);
        } catch (RetryableException e ) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible comunicarse con el servicio de consulta. " +
                    "Intenta lueguito", e);

        } catch(FeignException e ) {
            throw traducirErrorConsulta(e);
        }

        //  Validamos que venga un seguro y que no sea nulo
        if(consultaSeguroResponseDTO == null
                || consultaSeguroResponseDTO.getSeguros() == null
                || consultaSeguroResponseDTO.getSeguros().size() != 1) {
            throw respuestaConsultaInvalida();
        }

        // Extraemos el seguro que vino del otro microservicio (consultita)
        return consultaSeguroResponseDTO.getSeguros().get(0);
    }

    /* **--------------------------------** */
    private ResponseStatusException traducirErrorConsulta(FeignException e) {
        if(e.status() == 404) {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el seguro solicitado.", e);
        }
        return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No fue posible obtener la info del segurito.");
    }

    private ResponseStatusException respuestaConsultaInvalida() {
        return new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                "El servicio de consulta devolvió info incompleta o inválida del seguro");
    }
}

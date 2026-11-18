package com.example.verificota.cliente;

import com.example.verificota.dto.ConsultaSeguroResponseDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "consultita",
        url = "${clientes.consultita.url}"
)
public interface IConsultaSeguroCliente {

    /* ***** Consume el servicio de CONSULTA a la BD ***** */
    @GetMapping
    ConsultaSeguroResponseDTO buscarSegurosTodos();

    @GetMapping("/api/seguros/{idSeguro}")
    ConsultaSeguroResponseDTO consultarPorId(@PathVariable("idSeguro") Integer idSeguro);

    /* *********************************************************************************** */
    /* ***** Consume el servicio de CONSULTA al JSON ***** */
    @GetMapping("/api/seguros/json")
    ConsultaSeguroResponseDTO consultarJsonTodos();

    @GetMapping("/api/seguros/json/{idSeguro}")
    ConsultaSeguroResponseDTO consultarJsonPorId(@PathVariable("idSeguro") Integer idSeguro);
}

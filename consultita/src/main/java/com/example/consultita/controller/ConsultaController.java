package com.example.consultita.controller;

import com.example.consultita.dto.ResponseDTO;
import com.example.consultita.service.SeguroJsonService;
import com.example.consultita.service.SeguroService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seguros")
public class ConsultaController {

    private final SeguroService seguroService;
    private final SeguroJsonService seguroJsonService;

    public ConsultaController(SeguroService seguroService, SeguroJsonService seguroJsonService) {
        this.seguroService = seguroService;
        this.seguroJsonService = seguroJsonService;
    }

    /* ************* Consulta la BD ************ */
    @GetMapping
    public ResponseDTO buscarSegurosTodos() {
        return seguroService.consultaSeguros(null);
    }

    @GetMapping("/{idSeguro}")
    public ResponseDTO buscarSeguroPorId(@PathVariable("idSeguro") Integer idSeguro) {
        return seguroService.consultaSeguros(idSeguro);
    }

    /* ************* Consulta el archivo JSON ************ */
    @GetMapping("/json")
    public ResponseDTO buscarSegurosTodosJson() {
        return seguroJsonService.encontrarSeguroJson(null);
    }

    @GetMapping("/json/{idSeguro}")
    public ResponseDTO buscarSeguroPorIdJson(@PathVariable("idSeguro") Integer idSeguro) {
        return seguroJsonService.encontrarSeguroJson(idSeguro);
    }
}

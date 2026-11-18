package com.example.verificota.controller;

import com.example.verificota.dto.ValidaRequestDTO;
import com.example.verificota.dto.ValidaResponseDTO;
import com.example.verificota.service.ValidarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validaciones")
public class VerificaController {

    private final ValidarService validarService;

    public VerificaController(ValidarService validarService) {
        this.validarService = validarService;
    }

    /* ************* Valida con los datos de la BD ++++++++++ */
    @PostMapping
    public ResponseEntity<ValidaResponseDTO> validar(@RequestBody ValidaRequestDTO request){
        return validarService.validar(request);
    }

    @PostMapping("/json")
    public ResponseEntity<ValidaResponseDTO> validarJson(@RequestBody ValidaRequestDTO request){
        return validarService.validarJson(request);
    }
}

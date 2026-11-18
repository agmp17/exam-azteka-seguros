package com.example.consultita.controller;

import com.example.consultita.dtobis.RespCodigoMsgDTO;
import com.example.consultita.dtobis.ResponseBisDTO;
import com.example.consultita.dtobis.SeguroBisDTO;
import com.example.consultita.service.SeguroServiceBis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consulta-bis")
public class ConsultaControllerBis {

    private final SeguroServiceBis service;

    public ConsultaControllerBis(SeguroServiceBis service) {
        this.service = service;
    }

    @GetMapping
    public ResponseBisDTO buscarTodos() {
        return service.buscarTodos();
    }

    @GetMapping("{seguroId}")
    public ResponseBisDTO buscar1(@PathVariable Integer seguroId) {
        return service.buscar1(seguroId);
    }

    @DeleteMapping("/{seguroId}")
    public ResponseEntity<RespCodigoMsgDTO> borrarSeguro(@PathVariable Integer seguroId) {
        return service.borrarSeguro(seguroId);
    }

    @PostMapping
    public ResponseEntity<RespCodigoMsgDTO> insertSeguro(@RequestBody SeguroBisDTO seguroBisDTO) {
        return service.insertSeguro(seguroBisDTO);
    }

}

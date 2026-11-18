package com.example.consultita.service;

import com.example.consultita.dto.ResponseDTO;
import com.example.consultita.dto.SeguroDTO;
import com.example.consultita.repository.SeguroJsonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SeguroJsonService {

    private final SeguroJsonRepository seguroJsonRepository;

    public SeguroJsonService(SeguroJsonRepository seguroJsonRepository) {
        this.seguroJsonRepository = seguroJsonRepository;
    }

    public ResponseDTO encontrarSeguroJson(Integer idSeguro) {

        // Sin idSeguro: devuelve todos los seguros
        if(idSeguro == null){
            return seguroJsonRepository.encontrarTodosJson();
        }

        // CON identificador: Valida antes de leer el archivo
        if(idSeguro <= 0) {
            throw new ResponseStatusException(
                       HttpStatus.BAD_REQUEST, "El id ingresado no es válido. Verifica e intenta otra vez");
        }

        SeguroDTO seguroDTO = seguroJsonRepository.encontrarPorIdJson(idSeguro)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró información con el identificador ingresado. Asegúrate de que sea correcto"));

        return new ResponseDTO(List.of(seguroDTO));
    }
}

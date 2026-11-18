package com.example.consultita.service;

import com.example.consultita.dto.RequisitoDTO;
import com.example.consultita.dto.ResponseDTO;
import com.example.consultita.dto.SeguroDTO;
import com.example.consultita.entity.Seguro;
import com.example.consultita.entity.SeguroBeneficio;
import com.example.consultita.entity.SeguroGenero;
import com.example.consultita.repository.ISeguroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SeguroService {

    private final ISeguroRepository seguroRepository;

    public SeguroService(ISeguroRepository seguroRepository) {
        this.seguroRepository = seguroRepository;
    }

    @Transactional(readOnly = true)
    public ResponseDTO consultaSeguros(Integer idSeguro) {
        // Sin identificador, devuelve todos los seguros
        if(idSeguro == null) {
            List<SeguroDTO> seguroDTOList = seguroRepository.findAll()
                    .stream()
                    .map(this::seguroDTOBuild)
                    .toList();

            return new ResponseDTO(seguroDTOList);
        }

        // Con identificador, devuleve un seguro. Antes valida idSeguro
        if(idSeguro <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El identificador no es válido. Verifica y vuelve a intentarlo");
        }

        Seguro seguro = seguroRepository.findById(idSeguro)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró información con el identificador ingresado. Asegúrate de que sea correcto"));
        // Caso exitoso
        return new ResponseDTO(List.of(seguroDTOBuild(seguro)));
    }

    private SeguroDTO seguroDTOBuild (Seguro seguro) {
        // Obtenemos el listado de beneficios (unicamente la descripcion)
        List<String> beneficioList = seguro.getSeguroBeneficioList()
                .stream()
                .map(SeguroBeneficio::getDescripcion)
                .toList();

        // Obtenemos los generos permitidos en cada seguro (tabla seguro_genero)
        List<Integer> idGeneroList = seguro.getSeguroGeneroList()
                .stream()
                .map(SeguroGenero::getGeneroPermitido)
                .toList();

        // Construimos el objeto RequisitoDTO
        RequisitoDTO requisitoDTO = new RequisitoDTO(seguro.getEdadmin(), seguro.getEdadmax(), idGeneroList);

        return new SeguroDTO(seguro.getId(), seguro.getNombre(), seguro.getDescripcion(), seguro.getPrima(), beneficioList, requisitoDTO);
    }
}

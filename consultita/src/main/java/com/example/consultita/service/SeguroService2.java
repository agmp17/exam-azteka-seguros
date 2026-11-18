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
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeguroService2 {

    private final ISeguroRepository seguroRepository;

    public SeguroService2(ISeguroRepository seguroRepository) {
        this.seguroRepository = seguroRepository;
    }
    public ResponseDTO consultaSeguros(Integer idSeguro) {
        // Sin identificador, devuelve todos
        if(idSeguro == null) {
            List<Seguro> seguroList = seguroRepository.findAll();
            return convertirADTO(seguroList);
        }

        // Con identificador
        // primero validamos
        if(idSeguro <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El identificador solicitaDO no es válido");
        }

        Seguro seguro = seguroRepository.findById(idSeguro)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró información con el identificador ingresado. Asegúrate de que sea correcto"));

        List<Seguro> seguroList = new ArrayList<>();
        seguroList.add(seguro);
        return convertirADTO(seguroList);

    }

    private ResponseDTO convertirADTO(List<Seguro>  seguroList) {

        SeguroDTO seguroDTO;
        List<SeguroDTO> seguroDTOList = new ArrayList<>();
        ResponseDTO responseDTO = new ResponseDTO();

        for(Seguro item : seguroList) {
            seguroDTO = new SeguroDTO();
            seguroDTO.setId(item.getId());
            seguroDTO.setNombre(item.getNombre());
            seguroDTO.setDescripcion(item.getDescripcion());
            seguroDTO.setPrimaTotal(item.getPrima());

            List<String> beneficios = item.getSeguroBeneficioList()
                    .stream()
                    .map(SeguroBeneficio::getDescripcion)
                    .toList();

            List<Integer> generos = new ArrayList<>();
            for(SeguroGenero genero : item.getSeguroGeneroList()) {
                generos.add(genero.getGeneroPermitido());
            }

            seguroDTO.setBeneficios(beneficios);
            seguroDTO.setRequisitos(new RequisitoDTO(item.getEdadmin(), item.getEdadmax(), generos));

            seguroDTOList.add(seguroDTO);
        }

        responseDTO.setSeguros(seguroDTOList);
        return responseDTO;
    }

    /* ******************************************************* */




    private List<String> buildSeguroBeneficioList(List<SeguroBeneficio> seguroBeneficioList) {
        /* List<String> beneficios = new ArrayList<>();
        for(SeguroBeneficio item : seguroBeneficioList) {
            beneficios.add(item.getDescripcion());
        }
        return beneficios;
        */
        return seguroBeneficioList
                .stream()
                .map(SeguroBeneficio::getDescripcion)
                .toList();
    }

    private RequisitoDTO buildRequisitoDTO(Integer edadmin, Integer edadmax, List<SeguroGenero> seguroGeneroList) {
        return new RequisitoDTO(edadmin, edadmax, buildIdGeneros(seguroGeneroList));
    }

    private List<Integer> buildIdGeneros(List<SeguroGenero> seguroGeneroList) {
        List<Integer> ids = new ArrayList<>();
        for(SeguroGenero item : seguroGeneroList) {
            ids.add(item.getGeneroPermitido());
        }
        return ids;
    }
}

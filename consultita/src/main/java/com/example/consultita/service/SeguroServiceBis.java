package com.example.consultita.service;

import com.example.consultita.dtobis.*;
import com.example.consultita.entity.Seguro;
import com.example.consultita.entity.SeguroBeneficio;
import com.example.consultita.entity.SeguroGenero;
import com.example.consultita.repository.ISeguroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeguroServiceBis {
    private final ISeguroRepository repo;

    public SeguroServiceBis(ISeguroRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public ResponseBisDTO buscarTodos() {
        return new ResponseBisDTO( repo.findAll().stream()
                .map(this::toSeguroBisDTO)
                .toList());
    }

    @Transactional(readOnly = true)
    public ResponseBisDTO buscar1(Integer id) {
        Seguro seguro = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ese seguro no existe"));

        return new ResponseBisDTO(List.of(toSeguroBisDTO(seguro)));
    }

    @Transactional
    public ResponseEntity<RespCodigoMsgDTO> borrarSeguro(Integer seguroId) {
        Seguro seguro = repo.findById(seguroId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ese seguro no existe"));

        repo.delete(seguro);

        HttpStatus codigo = HttpStatus.OK;
        String msg = "Seguro Borrado";
        return ResponseEntity.status(codigo).body(new RespCodigoMsgDTO(codigo.value(), msg));
    }

    @Transactional
    public ResponseEntity<RespCodigoMsgDTO> insertSeguro(SeguroBisDTO seguroBisDTO) {
        Seguro seguro = toSeguro(seguroBisDTO);
        repo.save(seguro);

        HttpStatus codigo = HttpStatus.CREATED;
        String msg = "Seguro INsertado";
        return ResponseEntity.status(codigo).body(new RespCodigoMsgDTO(codigo.value(), msg));
    }
/* *********************************************************************************************** */
    private SeguroBisDTO toSeguroBisDTO(Seguro seguro) {

        // Obtenemos los beneficios
        List<BeneficioBisDTO> beneficios = seguro.getSeguroBeneficioList().stream()
                .map(beneficio -> new BeneficioBisDTO(
                        beneficio.getId(),
                        beneficio.getDescripcion()
                ))
                .toList();

        // Obtenemos los generos permitidos
        List<GeneroBisDTO> generos = seguro.getSeguroGeneroList().stream()
                .map(SeguroGenero -> new GeneroBisDTO(
                        SeguroGenero.getId(),
                        SeguroGenero.getGeneroPermitido()
                ))
                .toList();

        // Armamos el objeto Requisito
        RequisitoBisDTO requisitos = new RequisitoBisDTO(
                seguro.getEdadmin(),
                seguro.getEdadmax(),
                generos
        );

        return new SeguroBisDTO(
                seguro.getId(),
                seguro.getNombre(),
                seguro.getDescripcion(),
                seguro.getPrima(),
                beneficios,
                requisitos
        );
    }





    private Seguro toSeguro(SeguroBisDTO seguroBisDTO) {

        // Construimos los SeguroBeneficio
        List<SeguroBeneficio> seguroBeneficios = new ArrayList<>();

        for (BeneficioBisDTO item : seguroBisDTO.beneficios()) {
            SeguroBeneficio seguroBeneficio = new SeguroBeneficio();
            seguroBeneficio.setDescripcion(item.descripcion());
            seguroBeneficios.add(seguroBeneficio);
        }

        // Construimos los SeguroGenero
        List<SeguroGenero> seguroGeneros = new ArrayList<>();
        for (GeneroBisDTO item : seguroBisDTO.requisitos().genero()) {
            SeguroGenero seguroGenero = new SeguroGenero();
            seguroGenero.setGeneroPermitido(item.generoPermitido());
            seguroGeneros.add(seguroGenero);
        }

        // Construimos el Seguro
        Seguro seguro = new Seguro();
        seguro.setNombre(seguroBisDTO.nombre());
        seguro.setDescripcion(seguroBisDTO.descripcion());
        seguro.setPrima(seguroBisDTO.primaTotal());
        seguro.setEdadmin(seguroBisDTO.requisitos().edadMinima());
        seguro.setEdadmax(seguroBisDTO.requisitos().edadMaxima());
        seguro.setSeguroBeneficioList(seguroBeneficios);
        seguro.setSeguroGeneroList(seguroGeneros);

        return seguro;
    }
}

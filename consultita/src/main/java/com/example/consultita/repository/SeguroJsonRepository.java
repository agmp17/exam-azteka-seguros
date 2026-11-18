package com.example.consultita.repository;

import com.example.consultita.dto.ResponseDTO;
import com.example.consultita.dto.SeguroDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Repository
public class SeguroJsonRepository {

    private final JsonMapper jsonMapper;
    private final Resource archivo;

    public SeguroJsonRepository(JsonMapper jsonMapper, @Value("${seguros.archivo-json}") Resource archivo) {
        this.jsonMapper = jsonMapper;
        this.archivo = archivo;
    }

    private List<SeguroDTO> cargarArchivoJson() {

        try(InputStream inputStream = archivo.getInputStream()) {
            ResponseDTO responseDTO = jsonMapper.readValue(inputStream, ResponseDTO.class);
            return responseDTO.getSeguros();

        } catch(IOException | RuntimeException ex) {
            throw new IllegalStateException("No se pudo leer el archivo json", ex);
        }
    }

    public ResponseDTO encontrarTodosJson() {
        return new ResponseDTO(cargarArchivoJson());
    }

    public Optional<SeguroDTO> encontrarPorIdJson(Integer id) {
        return cargarArchivoJson().stream()
                .filter(seguroDTO -> seguroDTO.getId().equals(id))
                .findFirst();
    }
}

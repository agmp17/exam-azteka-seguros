package com.example.consultita.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequisitoDTO {

    private Integer edadMinima;
    private Integer edadMaxima;
    private List<Integer> idGeneros;
}

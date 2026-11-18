package com.example.consultita.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seguro_genero")
public class SeguroGenero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "genero_permitido")
    private Integer generoPermitido;

    @Column(name = "seguros_id", insertable = false, updatable = false)
    private Integer seguroId;
}

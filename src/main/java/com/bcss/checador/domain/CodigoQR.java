package com.bcss.checador.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodigoQR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCodigo;
    @OneToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

}
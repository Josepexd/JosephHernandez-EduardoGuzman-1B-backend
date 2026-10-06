package com._b.bossfinal.salon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Lo que el frontend usa para llenar el select de salones (y mostrar capacidad y precio)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalonResponseDTO {
    private Long idSalon;
    private String nombreSalon;
    private Integer capacidad;
    private BigDecimal precioRenta;
    private String ubicacion;
}

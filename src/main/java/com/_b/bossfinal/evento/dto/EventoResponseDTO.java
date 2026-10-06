package com._b.bossfinal.evento.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

// Ya trae el nombre completo del cliente y el nombre del salon para pintar la tabla sin hacer mas peticiones
// Tambien incluyo idCliente e idSalon para precargar los select cuando edito un evento
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoResponseDTO {
    private Long idEvento;
    private Long idCliente;
    private String nombreCliente;
    private Long idSalon;
    private String nombreSalon;
    private String nombreEvento;
    private LocalDate fechaEvento;
    private Integer cantidadPersonas;
    private Integer cantidadHoras;
    private String estado;
    private BigDecimal totalPago;
}

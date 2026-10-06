package com._b.bossfinal.evento.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// Aqui NO existe totalPago a proposito: el total lo calcula el backend y el usuario no lo puede mandar
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoRequestDTO {

    @NotNull(message = "Debes seleccionar un cliente")
    private Long idCliente;

    @NotNull(message = "Debes seleccionar un salon")
    private Long idSalon;

    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(max = 100, message = "El nombre del evento no puede pasar de 100 caracteres")
    private String nombreEvento;

    // Llega como texto yyyy-MM-dd (el formato del input type="date") y Jackson lo convierte a LocalDate
    @NotNull(message = "La fecha del evento es obligatoria")
    private LocalDate fechaEvento;

    @NotNull(message = "La cantidad de personas es obligatoria")
    @Min(value = 1, message = "La cantidad de personas debe ser mayor a cero")
    private Integer cantidadPersonas;

    @NotNull(message = "La cantidad de horas es obligatoria")
    @Min(value = 1, message = "La cantidad de horas debe ser mayor a cero")
    private Integer cantidadHoras;

    // Solo lo uso al editar. Al crear lo ignoro porque todo evento nuevo nace con el estado inicial
    private String estado;
}

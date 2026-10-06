package com._b.bossfinal.salon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Tabla SALONES. Los salones se cargan directo en la base, desde la API solo los leo
@Entity
@Table(name = "SALONES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SALON")
    private Long idSalon;

    @Column(name = "NOMBRE_SALON", nullable = false, length = 100)
    private String nombreSalon;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    // NUMBER(10,2) en Oracle, uso BigDecimal para no perder precision con el dinero
    @Column(name = "PRECIO_RENTA", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioRenta;

    @Column(name = "UBICACION", length = 100)
    private String ubicacion;
}

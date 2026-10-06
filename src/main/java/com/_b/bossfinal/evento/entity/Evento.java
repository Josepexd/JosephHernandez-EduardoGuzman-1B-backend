package com._b.bossfinal.evento.entity;

import com._b.bossfinal.cliente.entity.Cliente;
import com._b.bossfinal.salon.entity.Salon;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

// Tabla EVENTOS. Tiene dos llaves foraneas: ID_CLIENTE hacia CLIENTES e ID_SALON hacia SALONES
@Entity
@Table(name = "EVENTOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EVENTO")
    private Long idEvento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CLIENTE", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_SALON", nullable = false)
    private Salon salon;

    @Column(name = "NOMBRE_EVENTO", nullable = false, length = 100)
    private String nombreEvento;

    @Column(name = "FECHA_EVENTO", nullable = false)
    private LocalDate fechaEvento;

    @Column(name = "CANTIDAD_PERSONAS", nullable = false)
    private Integer cantidadPersonas;

    @Column(name = "CANTIDAD_HORAS")
    private Integer cantidadHoras;

    @Column(name = "ESTADO", length = 20)
    private String estado;

    @Column(name = "TOTAL_PAGO", precision = 8, scale = 2)
    private BigDecimal totalPago;
}

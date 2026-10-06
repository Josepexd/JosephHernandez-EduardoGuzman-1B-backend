package com._b.bossfinal.evento.service;

import com._b.bossfinal.cliente.entity.Cliente;
import com._b.bossfinal.cliente.repository.ClienteRepository;
import com._b.bossfinal.evento.dto.EventoRequestDTO;
import com._b.bossfinal.evento.dto.EventoResponseDTO;
import com._b.bossfinal.evento.entity.Evento;
import com._b.bossfinal.evento.mapper.EventoMapper;
import com._b.bossfinal.evento.repository.EventoRepository;
import com._b.bossfinal.exception.BusinessException;
import com._b.bossfinal.exception.ResourceNotFoundException;
import com._b.bossfinal.salon.entity.Salon;
import com._b.bossfinal.salon.repository.SalonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventoService {

    // Dejo los estados en un solo lugar: si mi profe cambia los nombres (CONFIRMADA, COMPLETADA...) solo toco aqui
    public static final String ESTADO_INICIAL = "CONFIRMADO";
    private static final Set<String> ESTADOS_PERMITIDOS = Set.of("PENDIENTE", "CONFIRMADO", "CANCELADO", "FINALIZADO");
    // Un salon queda ocupado solo si el evento esta en uno de estos estados
    private static final Set<String> ESTADOS_ACTIVOS = Set.of("PENDIENTE", "CONFIRMADO");
    // TOTAL_PAGO es NUMBER(8,2), este es el maximo que cabe en la columna
    private static final BigDecimal TOTAL_MAXIMO = new BigDecimal("999999.99");

    private final EventoRepository eventoRepository;
    private final ClienteRepository clienteRepository;
    private final SalonRepository salonRepository;
    private final EventoMapper eventoMapper;

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> consultarEventos() {
        return eventoMapper.toResponseList(eventoRepository.findAllConDetalle());
    }

    @Transactional(readOnly = true)
    public EventoResponseDTO consultarEventoId(Long id) {
        return eventoMapper.toResponse(buscarEvento(id));
    }

    @Transactional
    public EventoResponseDTO crearEvento(EventoRequestDTO dto) {
        Cliente cliente = buscarCliente(dto.getIdCliente());
        Salon salon = buscarSalon(dto.getIdSalon());

        // Reglas de negocio: primero capacidad y luego que el salon este libre ese dia
        validarCapacidad(dto.getCantidadPersonas(), salon);
        validarFechaDisponible(salon.getIdSalon(), dto.getFechaEvento(), ESTADO_INICIAL, null);

        Evento evento = eventoMapper.toEntity(dto);
        evento.setCliente(cliente);
        evento.setSalon(salon);
        // El estado inicial lo pongo yo, aunque el JSON traiga otro lo ignoro
        evento.setEstado(ESTADO_INICIAL);
        // El total lo calculo yo en el backend: horas por precio de renta del salon
        evento.setTotalPago(calcularTotal(dto.getCantidadHoras(), salon));

        return eventoMapper.toResponse(eventoRepository.save(evento));
    }

    @Transactional
    public EventoResponseDTO actualizarEvento(Long id, EventoRequestDTO dto) {
        Evento evento = buscarEvento(id);
        Cliente cliente = buscarCliente(dto.getIdCliente());
        Salon salon = buscarSalon(dto.getIdSalon());

        validarCapacidad(dto.getCantidadPersonas(), salon);

        // Si el JSON no trae estado dejo el que ya tenia; si lo trae, tiene que ser uno permitido
        String estadoFinal = (dto.getEstado() == null || dto.getEstado().isBlank())
                ? evento.getEstado()
                : normalizarEstado(dto.getEstado());
        validarFechaDisponible(salon.getIdSalon(), dto.getFechaEvento(), estadoFinal, id);

        eventoMapper.updateEntity(dto, evento);
        evento.setCliente(cliente);
        evento.setSalon(salon);
        evento.setEstado(estadoFinal);
        // Recalculo el total por si cambiaron las horas o el salon
        evento.setTotalPago(calcularTotal(dto.getCantidadHoras(), salon));

        return eventoMapper.toResponse(eventoRepository.save(evento));
    }

    // El cambio de estado desde la tabla: solo toca el estado y revisa que sea un valor permitido
    @Transactional
    public EventoResponseDTO cambiarEstado(Long id, String estado) {
        Evento evento = buscarEvento(id);
        String nuevoEstado = normalizarEstado(estado);

        // Si reactivo un evento cancelado, tengo que revisar que el salon no se haya ocupado mientras tanto
        validarFechaDisponible(evento.getSalon().getIdSalon(), evento.getFechaEvento(), nuevoEstado, id);

        evento.setEstado(nuevoEstado);
        return eventoMapper.toResponse(eventoRepository.save(evento));
    }

    @Transactional
    public void eliminarEvento(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe un evento con el id " + id);
        }
        eventoRepository.deleteById(id);
    }

    // ---------- metodos privados de apoyo ----------

    private Evento buscarEvento(Long id) {
        return eventoRepository.findByIdConDetalle(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un evento con el id " + id));
    }

    private Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un cliente con el id " + id));
    }

    private Salon buscarSalon(Long id) {
        return salonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un salon con el id " + id));
    }

    private void validarCapacidad(Integer cantidadPersonas, Salon salon) {
        if (cantidadPersonas > salon.getCapacidad()) {
            throw new BusinessException("La cantidad de personas (" + cantidadPersonas
                    + ") supera la capacidad maxima del salon '" + salon.getNombreSalon()
                    + "' (" + salon.getCapacidad() + ")");
        }
    }

    // Solo reviso choque de fechas si el evento queda en un estado activo; uno cancelado o finalizado no ocupa el salon
    private void validarFechaDisponible(Long idSalon, LocalDate fecha, String estado, Long idEventoExcluido) {
        if (estado == null || !ESTADOS_ACTIVOS.contains(estado)) {
            return;
        }
        boolean ocupado = (idEventoExcluido == null)
                ? eventoRepository.existsBySalon_IdSalonAndFechaEventoAndEstadoIn(idSalon, fecha, ESTADOS_ACTIVOS)
                : eventoRepository.existsBySalon_IdSalonAndFechaEventoAndEstadoInAndIdEventoNot(idSalon, fecha, ESTADOS_ACTIVOS, idEventoExcluido);
        if (ocupado) {
            throw new BusinessException("El salon ya tiene un evento activo en la fecha " + fecha);
        }
    }

    private String normalizarEstado(String estado) {
        String normalizado = estado == null ? "" : estado.trim().toUpperCase();
        if (!ESTADOS_PERMITIDOS.contains(normalizado)) {
            throw new BusinessException("Estado no permitido. Los valores validos son: PENDIENTE, CONFIRMADO, CANCELADO, FINALIZADO");
        }
        return normalizado;
    }

    private BigDecimal calcularTotal(Integer cantidadHoras, Salon salon) {
        BigDecimal total = salon.getPrecioRenta()
                .multiply(BigDecimal.valueOf(cantidadHoras))
                .setScale(2, RoundingMode.HALF_UP);
        if (total.compareTo(TOTAL_MAXIMO) > 0) {
            throw new BusinessException("El total a pagar (" + total + ") supera el maximo permitido de " + TOTAL_MAXIMO);
        }
        return total;
    }
}

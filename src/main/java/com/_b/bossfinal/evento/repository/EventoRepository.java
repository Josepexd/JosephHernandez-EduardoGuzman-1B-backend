package com._b.bossfinal.evento.repository;

import com._b.bossfinal.evento.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    // JOIN FETCH para traer cliente y salon en una sola consulta y no hacer una consulta por cada fila de la tabla
    @Query("select e from Evento e join fetch e.cliente join fetch e.salon order by e.fechaEvento desc, e.idEvento desc")
    List<Evento> findAllConDetalle();

    @Query("select e from Evento e join fetch e.cliente join fetch e.salon where e.idEvento = :id")
    Optional<Evento> findByIdConDetalle(@Param("id") Long id);

    // Lo uso desde ClienteService para no dejar eliminar un cliente que ya tiene eventos
    boolean existsByCliente_IdCliente(Long idCliente);

    // Validacion de fecha: reviso si el salon ya tiene un evento activo ese dia
    boolean existsBySalon_IdSalonAndFechaEventoAndEstadoIn(Long idSalon, LocalDate fechaEvento, Collection<String> estados);

    // Lo mismo pero para editar: excluyo el propio evento para que no choque consigo mismo
    boolean existsBySalon_IdSalonAndFechaEventoAndEstadoInAndIdEventoNot(Long idSalon, LocalDate fechaEvento, Collection<String> estados, Long idEvento);
}

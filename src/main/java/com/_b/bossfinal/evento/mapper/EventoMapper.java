package com._b.bossfinal.evento.mapper;

import com._b.bossfinal.evento.dto.EventoRequestDTO;
import com._b.bossfinal.evento.dto.EventoResponseDTO;
import com._b.bossfinal.evento.entity.Evento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    @Mapping(target = "idEvento", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "salon", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "totalPago", ignore = true)
    Evento toEntity(EventoRequestDTO dto);

    @Mapping(source = "cliente.idCliente", target = "idCliente")
    @Mapping(target = "nombreCliente", expression = "java(evento.getCliente().getNombre() + \" \" + evento.getCliente().getApellido())")
    @Mapping(source = "salon.idSalon", target = "idSalon")
    @Mapping(source = "salon.nombreSalon", target = "nombreSalon")
    EventoResponseDTO toResponse(Evento evento);

    List<EventoResponseDTO> toResponseList(List<Evento> eventos);

    @Mapping(target = "idEvento", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "salon", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "totalPago", ignore = true)
    void updateEntity(EventoRequestDTO dto, @MappingTarget Evento evento);
}

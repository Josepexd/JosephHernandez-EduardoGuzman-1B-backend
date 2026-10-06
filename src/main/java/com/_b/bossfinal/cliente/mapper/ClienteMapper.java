package com._b.bossfinal.cliente.mapper;

import com._b.bossfinal.cliente.dto.ClienteRequestDTO;
import com._b.bossfinal.cliente.dto.ClienteResponseDTO;
import com._b.bossfinal.cliente.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "idCliente", ignore = true)
    Cliente toEntity(ClienteRequestDTO dto);

    ClienteResponseDTO toResponse(Cliente cliente);

    List<ClienteResponseDTO> toResponseList(List<Cliente> clienteEntities);

    @Mapping(target = "idCliente", ignore = true)
    void updateEntity(ClienteRequestDTO dto, @MappingTarget Cliente cliente);
}

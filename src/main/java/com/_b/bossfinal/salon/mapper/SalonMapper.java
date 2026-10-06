package com._b.bossfinal.salon.mapper;

import com._b.bossfinal.salon.dto.SalonResponseDTO;
import com._b.bossfinal.salon.entity.Salon;
import org.mapstruct.Mapper;

import java.util.List;

// Solo necesito convertir de entidad a DTO porque los salones no se insertan desde la API
@Mapper(componentModel = "spring")
public interface SalonMapper {

    SalonResponseDTO toResponse(Salon salon);

    List<SalonResponseDTO> toResponseList(List<Salon> salones);
}

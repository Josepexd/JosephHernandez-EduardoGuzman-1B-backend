package com._b.bossfinal.salon.service;

import com._b.bossfinal.exception.ResourceNotFoundException;
import com._b.bossfinal.salon.dto.SalonResponseDTO;
import com._b.bossfinal.salon.mapper.SalonMapper;
import com._b.bossfinal.salon.repository.SalonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Le agregue @Service y las dependencias que le faltaban; sin eso Spring nunca lo creaba y todo devolvia null
@Service
@RequiredArgsConstructor
public class SalonService {

    private final SalonRepository salonRepository;
    private final SalonMapper salonMapper;

    @Transactional(readOnly = true)
    public SalonResponseDTO ConsultarSalonId(Long id) {
        return salonRepository.findById(id)
                .map(salonMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un salon con el id " + id));
    }

    @Transactional(readOnly = true)
    public List<SalonResponseDTO> consultarSalones() {
        return salonMapper.toResponseList(salonRepository.findAll());
    }
}

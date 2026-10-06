package com._b.bossfinal.salon.controller;

import com._b.bossfinal.response.ApiResponse;
import com._b.bossfinal.salon.dto.SalonResponseDTO;
import com._b.bossfinal.salon.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Quite @AllArgsConstructor y @NoArgsConstructor porque con los dos Spring usaba el vacio y el service quedaba en null;
// con @RequiredArgsConstructor y el campo final se inyecta bien
@RestController
@RequestMapping("/salon")
@RequiredArgsConstructor
public class SalonController {

    private final SalonService salonService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SalonResponseDTO>> ConsultarSalonId(@PathVariable Long id) {
        SalonResponseDTO responseDTO = salonService.ConsultarSalonId(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "salon consultado con exito", responseDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SalonResponseDTO>>> consultarSalones() {
        List<SalonResponseDTO> salones = salonService.consultarSalones();
        return ResponseEntity.ok(new ApiResponse<>(true, "salones consultados con exito", salones));
    }
}

package com._b.bossfinal.evento.controller;

import com._b.bossfinal.evento.dto.EventoRequestDTO;
import com._b.bossfinal.evento.dto.EventoResponseDTO;
import com._b.bossfinal.evento.service.EventoService;
import com._b.bossfinal.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/evento")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventoResponseDTO>>> consultarEventos() {
        List<EventoResponseDTO> eventos = eventoService.consultarEventos();
        return ResponseEntity.ok(new ApiResponse<>(true, "Eventos consultados con exito", eventos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventoResponseDTO>> consultarEventoId(@PathVariable Long id) {
        EventoResponseDTO evento = eventoService.consultarEventoId(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Evento consultado con exito", evento));
    }

    // El @Valid hace que se ejecuten las validaciones del DTO antes de entrar al metodo; si fallan responde el handler global
    @PostMapping
    public ResponseEntity<ApiResponse<EventoResponseDTO>> crearEvento(@Valid @RequestBody EventoRequestDTO dto) {
        EventoResponseDTO creado = eventoService.crearEvento(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Evento registrado con exito", creado));
    }

    // El id viaja por la URL (@PathVariable) y los datos nuevos por el JSON del body
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventoResponseDTO>> actualizarEvento(@PathVariable Long id,
                                                                           @Valid @RequestBody EventoRequestDTO dto) {
        EventoResponseDTO actualizado = eventoService.actualizarEvento(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Evento actualizado con exito", actualizado));
    }

    // Ruta aparte solo para cambiar el estado desde la tabla, ejemplo: PUT /evento/5/estado?estado=CANCELADO
    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<EventoResponseDTO>> cambiarEstado(@PathVariable Long id,
                                                                        @RequestParam String estado) {
        EventoResponseDTO actualizado = eventoService.cambiarEstado(id, estado);
        return ResponseEntity.ok(new ApiResponse<>(true, "Estado del evento actualizado con exito", actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarEvento(@PathVariable Long id) {
        eventoService.eliminarEvento(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Evento eliminado con exito"));
    }
}

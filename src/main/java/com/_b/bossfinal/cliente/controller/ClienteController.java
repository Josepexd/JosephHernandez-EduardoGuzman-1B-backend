package com._b.bossfinal.cliente.controller;

import com._b.bossfinal.cliente.dto.ClienteRequestDTO;
import com._b.bossfinal.cliente.dto.ClienteResponseDTO;
import com._b.bossfinal.cliente.service.ClienteService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteResponseDTO>>> consultarClientes() {
        List<ClienteResponseDTO> clientes = clienteService.consultarClientes();
        return ResponseEntity.ok(new ApiResponse<>(true, "Clientes consultados con exito", clientes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> consultarClienteId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.consultarClienteId(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cliente consultado con exito", cliente));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> crearCliente(@Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO creado = clienteService.crearCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Cliente registrado con exito", creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> actualizarCliente(@PathVariable Long id,
                                                                             @Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO actualizado = clienteService.actualizarCliente(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cliente actualizado con exito", actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cliente eliminado con exito"));
    }
}

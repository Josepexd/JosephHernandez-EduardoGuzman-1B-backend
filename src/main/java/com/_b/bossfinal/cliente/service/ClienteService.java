package com._b.bossfinal.cliente.service;

import com._b.bossfinal.cliente.dto.ClienteRequestDTO;
import com._b.bossfinal.cliente.dto.ClienteResponseDTO;
import com._b.bossfinal.cliente.entity.Cliente;
import com._b.bossfinal.cliente.mapper.ClienteMapper;
import com._b.bossfinal.cliente.repository.ClienteRepository;
import com._b.bossfinal.evento.repository.EventoRepository;
import com._b.bossfinal.exception.BusinessException;
import com._b.bossfinal.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EventoRepository eventoRepository;
    private final ClienteMapper clienteMapper;

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> consultarClientes() {
        return clienteMapper.toResponseList(clienteRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO consultarClienteId(Long id) {
        return clienteMapper.toResponse(buscarCliente(id));
    }

    @Transactional
    public ClienteResponseDTO crearCliente(ClienteRequestDTO dto) {
        limpiarCampos(dto);
        if (clienteRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new BusinessException("Ya existe un cliente registrado con el email " + dto.getEmail());
        }
        Cliente cliente = clienteMapper.toEntity(dto);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO dto) {
        limpiarCampos(dto);
        Cliente cliente = buscarCliente(id);
        if (clienteRepository.existsByEmailIgnoreCaseAndIdClienteNot(dto.getEmail(), id)) {
            throw new BusinessException("Ya existe otro cliente registrado con el email " + dto.getEmail());
        }
        clienteMapper.updateEntity(dto, cliente);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public void eliminarCliente(Long id) {
        Cliente cliente = buscarCliente(id);
        if (eventoRepository.existsByCliente_IdCliente(id)) {
            throw new BusinessException("No se puede eliminar al cliente porque tiene eventos registrados");
        }
        clienteRepository.delete(cliente);
    }

    private Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un cliente con el id " + id));
    }

    private void limpiarCampos(ClienteRequestDTO dto) {
        dto.setNombre(dto.getNombre().trim());
        dto.setApellido(dto.getApellido().trim());
        dto.setTelefono(dto.getTelefono().trim());
        dto.setEmail(dto.getEmail().trim());
        if (dto.getDireccion() != null) {
            dto.setDireccion(dto.getDireccion().trim());
        }
    }
}

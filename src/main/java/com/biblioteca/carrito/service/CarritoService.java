package com.biblioteca.carrito.service;

import com.biblioteca.carrito.client.JuegoClient;
import com.biblioteca.carrito.client.UsuarioClient;
import com.biblioteca.carrito.dto.CantidadRequestDTO;
import com.biblioteca.carrito.dto.CarritoRequestDTO;
import com.biblioteca.carrito.dto.CarritoResponseDTO;
import com.biblioteca.carrito.dto.JuegoDTO;
import com.biblioteca.carrito.model.CarritoItem;
import com.biblioteca.carrito.repository.CarritoRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final JuegoClient juegoClient;
    private final UsuarioClient usuarioClient;

    public List<CarritoResponseDTO> obtenerTodos() {
        return carritoRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public Optional<CarritoResponseDTO> obtenerPorId(Long id) {
        return carritoRepository.findById(id).map(this::mapToDTO);
    }

    public List<CarritoResponseDTO> obtenerPorUsuario(Long usuarioId) {
        validarUsuario(usuarioId);
        return carritoRepository.findByUsuarioId(usuarioId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional
    public CarritoResponseDTO agregar(CarritoRequestDTO dto) {
        validarUsuario(dto.getUsuarioId());
        validarJuego(dto.getJuegoId());

        CarritoItem item = carritoRepository
                .findByUsuarioIdAndJuegoId(dto.getUsuarioId(), dto.getJuegoId())
                .map(existente -> {
                    existente.setCantidad(existente.getCantidad() + dto.getCantidad());
                    return existente;
                })
                .orElseGet(() -> new CarritoItem(
                        null,
                        dto.getUsuarioId(),
                        dto.getJuegoId(),
                        dto.getCantidad(),
                        LocalDateTime.now()
                ));

        return mapToDTO(carritoRepository.save(item));
    }

    @Transactional
    public Optional<CarritoResponseDTO> actualizarCantidad(Long id, CantidadRequestDTO dto) {
        return carritoRepository.findById(id).map(item -> {
            item.setCantidad(dto.getCantidad());
            return mapToDTO(carritoRepository.save(item));
        });
    }

    @Transactional
    public void eliminar(Long id) {
        if (!carritoRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un item de carrito con id " + id);
        }
        carritoRepository.deleteById(id);
    }

    @Transactional
    public void vaciarPorUsuario(Long usuarioId) {
        validarUsuario(usuarioId);
        carritoRepository.deleteByUsuarioId(usuarioId);
    }

    private CarritoResponseDTO mapToDTO(CarritoItem item) {
        JuegoDTO juego = obtenerJuegoSeguro(item.getJuegoId());
        return new CarritoResponseDTO(
                item.getId(),
                item.getUsuarioId(),
                item.getJuegoId(),
                item.getCantidad(),
                item.getAgregadoEn(),
                juego
        );
    }

    private void validarUsuario(Long usuarioId) {
        try {
            usuarioClient.obtenerUsuario(usuarioId);
        } catch (WebClientResponseException.NotFound e) {
            throw new IllegalArgumentException("No existe un usuario con id " + usuarioId);
        }
    }

    private void validarJuego(Long juegoId) {
        try {
            juegoClient.obtenerJuego(juegoId);
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("No existe un juego con id " + juegoId);
        }
    }

    private JuegoDTO obtenerJuegoSeguro(Long juegoId) {
        try {
            return juegoClient.obtenerJuego(juegoId);
        } catch (FeignException e) {
            return null;
        }
    }
}

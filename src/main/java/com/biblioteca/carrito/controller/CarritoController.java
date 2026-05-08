package com.biblioteca.carrito.controller;

import com.biblioteca.carrito.dto.CantidadRequestDTO;
import com.biblioteca.carrito.dto.CarritoRequestDTO;
import com.biblioteca.carrito.dto.CarritoResponseDTO;
import com.biblioteca.carrito.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping
    public List<CarritoResponseDTO> obtenerTodos() {
        return carritoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return carritoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<CarritoResponseDTO> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return carritoService.obtenerPorUsuario(usuarioId);
    }

    @PostMapping
    public ResponseEntity<CarritoResponseDTO> agregar(@Valid @RequestBody CarritoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.agregar(dto));
    }

    @PatchMapping("/{id}/cantidad")
    public ResponseEntity<CarritoResponseDTO> actualizarCantidad(
            @PathVariable Long id,
            @Valid @RequestBody CantidadRequestDTO dto) {
        return carritoService.actualizarCantidad(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        carritoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<Void> vaciarPorUsuario(@PathVariable Long usuarioId) {
        carritoService.vaciarPorUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }
}

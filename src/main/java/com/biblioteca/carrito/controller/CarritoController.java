package com.biblioteca.carrito.controller;

import com.biblioteca.carrito.dto.CantidadRequestDTO;
import com.biblioteca.carrito.dto.CarritoRequestDTO;
import com.biblioteca.carrito.dto.CarritoResponseDTO;
import com.biblioteca.carrito.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v2/carrito")
@Tag(name = "Carrito", description = "Operaciones del carrito de compras")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping
    @Operation(summary = "Listar todos los elementos del carrito")
    public List<CarritoResponseDTO> obtenerTodos() {
        return carritoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un elemento del carrito por ID")
    public ResponseEntity<CarritoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return carritoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar el carrito de un usuario")
    public List<CarritoResponseDTO> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return carritoService.obtenerPorUsuario(usuarioId);
    }

    @PostMapping
    @Operation(summary = "Agregar un juego al carrito")
    public ResponseEntity<CarritoResponseDTO> agregar(@Valid @RequestBody CarritoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.agregar(dto));
    }

    @PatchMapping("/{id}/cantidad")
    @Operation(summary = "Actualizar la cantidad de un elemento")
    public ResponseEntity<CarritoResponseDTO> actualizarCantidad(
            @PathVariable Long id,
            @Valid @RequestBody CantidadRequestDTO dto) {
        return carritoService.actualizarCantidad(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un elemento del carrito")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        carritoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/usuario/{usuarioId}")
    @Operation(summary = "Vaciar el carrito de un usuario")
    public ResponseEntity<Void> vaciarPorUsuario(@PathVariable Long usuarioId) {
        carritoService.vaciarPorUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }
}

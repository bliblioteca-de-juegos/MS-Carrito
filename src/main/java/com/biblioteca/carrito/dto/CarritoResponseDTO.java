package com.biblioteca.carrito.dto;

import java.time.LocalDateTime;

public record CarritoResponseDTO(
        Long id,
        Long usuarioId,
        Long juegoId,
        Integer cantidad,
        LocalDateTime agregadoEn,
        JuegoDTO juego
) {
}

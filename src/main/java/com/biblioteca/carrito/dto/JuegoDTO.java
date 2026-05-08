package com.biblioteca.carrito.dto;

public record JuegoDTO(
        Long id,
        String nombre,
        String titulo,
        String descripcion,
        Double precio
) {

    public String nombreVisible() {
        return nombre != null ? nombre : titulo;
    }
}

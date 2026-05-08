CREATE TABLE carrito_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    juego_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    agregado_en DATETIME NOT NULL,
    CONSTRAINT uk_carrito_usuario_juego UNIQUE (usuario_id, juego_id)
);

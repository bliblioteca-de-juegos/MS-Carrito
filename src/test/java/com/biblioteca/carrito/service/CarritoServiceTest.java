package com.biblioteca.carrito.service;

import com.biblioteca.carrito.client.JuegoClient;
import com.biblioteca.carrito.client.UsuarioClient;
import com.biblioteca.carrito.dto.CarritoResponseDTO;
import com.biblioteca.carrito.model.CarritoItem;
import com.biblioteca.carrito.repository.CarritoRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private JuegoClient juegoClient;
    @Mock
    private UsuarioClient usuarioClient;
    @InjectMocks
    private CarritoService carritoService;

    private final Faker faker = new Faker();

    @Test
    void obtenerPorIdRetornaElItemDelCarrito() {
        Long id = faker.number().numberBetween(1L, 1000L);
        Long usuarioId = faker.number().numberBetween(1L, 1000L);
        Long juegoId = faker.number().numberBetween(1L, 1000L);
        CarritoItem item = new CarritoItem(id, usuarioId, juegoId, 2, LocalDateTime.now());
        when(carritoRepository.findById(id)).thenReturn(Optional.of(item));

        Optional<CarritoResponseDTO> resultado = carritoService.obtenerPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(usuarioId, resultado.get().usuarioId());
        assertEquals(juegoId, resultado.get().juegoId());
        assertEquals(2, resultado.get().cantidad());
    }

    @Test
    void eliminarLanzaExcepcionCuandoElItemNoExiste() {
        Long id = faker.number().numberBetween(1L, 1000L);
        when(carritoRepository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> carritoService.eliminar(id));
        verify(carritoRepository, never()).deleteById(id);
    }
}

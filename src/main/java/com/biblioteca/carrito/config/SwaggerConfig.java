package com.biblioteca.carrito.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI carritoOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API MS-Carrito")
                .version("1.0")
                .description("Gestion del carrito de compras de la biblioteca de juegos."));
    }
}

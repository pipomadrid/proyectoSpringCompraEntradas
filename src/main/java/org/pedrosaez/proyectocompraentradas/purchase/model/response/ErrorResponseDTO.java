package org.pedrosaez.proyectocompraentradas.purchase.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Estructura estándar de error en la API")
public record ErrorResponseDTO(

    @Schema(description = "Código HTTP del error", example = "400")
    int status,

    @Schema(description = "Código interno del error", example = "PURCHASE_001")
    String code,

    @Schema(description = "Mensaje descriptivo del error",
            example = "El evento no existe")
    String message,

    @Schema(description = "Fecha y hora del error",
            example = "2026-03-01T19:45:00")
    LocalDateTime timestamp,

    @Schema(description = "Ruta de la petición que generó el error",
            example = "/api/purchases")
    String path) {}

package org.pedrosaez.proyectocompraentradas.purchase.controller;


import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.pedrosaez.proyectocompraentradas.purchase.controller.error.PurchaseException;
import org.pedrosaez.proyectocompraentradas.purchase.model.request.PurchaseRequestDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.CBResponseDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.CustomPurchaseResponseDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.ErrorResponseDTO;
import org.pedrosaez.proyectocompraentradas.purchase.service.PurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Compras",description="Operaciones relacionadas con compras de entradas de eventos")
@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    private PurchaseService purchaseService;


    @Operation(
            summary = "Procesar compra de entradas",
            description = "Crea una nueva compra de entradas y devuelve la información del proceso realizado"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Compra creada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomPurchaseResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Servicio temporalmente no disponible (Circuit Breaker activado)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CBResponseDTO.class)
                    )
            )
    })
    @CircuitBreaker(name = "purchaseCircuitBreaker", fallbackMethod = "fallBackCreatePurchase")
    @PostMapping
    public ResponseEntity<?> createPurchase(@Parameter(
            description = "Datos necesarios para realizar la compra",
            required = true
    ) @Valid @RequestBody PurchaseRequestDTO purchaseDTO) {

        logger.info("------ createPurchase (POST)");
        CustomPurchaseResponseDTO result = purchaseService.compraEntradas(purchaseDTO);
        logger.info("------ Purchase created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    private ResponseEntity<CBResponseDTO> serviceUnavailableResponse(String message) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new CBResponseDTO(
                        message,
                        HttpStatus.SERVICE_UNAVAILABLE
                ));
    }

    public ResponseEntity<CBResponseDTO> fallBackCreatePurchase(PurchaseRequestDTO purchaseDTO, FeignException.InternalServerError e) {
        String message = "El servicio de compras inestable, pruebe de nuevo";
        return serviceUnavailableResponse(message);
    }

    public ResponseEntity<CBResponseDTO> fallBackCreatePurchase(PurchaseRequestDTO purchaseDTO, CallNotPermittedException e) {
        String message = "El servicio de compras no está disponible ahora mismo. Intenta más tarde.";
        return serviceUnavailableResponse(message);
    }
}

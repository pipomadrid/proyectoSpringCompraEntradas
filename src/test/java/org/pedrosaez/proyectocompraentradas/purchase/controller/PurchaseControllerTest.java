package org.pedrosaez.proyectocompraentradas.purchase.controller;

import feign.FeignException;
import feign.Request;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pedrosaez.proyectocompraentradas.purchase.model.request.PurchaseRequestDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.CBResponseDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.CustomPurchaseResponseDTO;
import org.pedrosaez.proyectocompraentradas.purchase.service.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseControllerTest {

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController purchaseController;

    private PurchaseRequestDTO purchaseRequestDTO;
    private CustomPurchaseResponseDTO customPurchaseResponseDTO;

    @BeforeEach
    void setUp() {
        purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.setEventId(1L);

        customPurchaseResponseDTO = new CustomPurchaseResponseDTO(
                "Compra confirmada, 🎉 Disfruta del Evento 🎉",
                "Concierto Rock",
                "50.00",
                "01/01/2025 10:00:00",
                "Pedro Saez"
        );
    }

    // ─── createPurchase ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe devolver 201 CREATED con el resultado cuando la compra es exitosa")
    void shouldReturn201CreatedWhenPurchaseIsSuccessful() {
        // Given
        when(purchaseService.compraEntradas(purchaseRequestDTO)).thenReturn(customPurchaseResponseDTO);

        // When
        ResponseEntity<?> response = purchaseController.createPurchase(purchaseRequestDTO);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(customPurchaseResponseDTO);
        verify(purchaseService, times(1)).compraEntradas(purchaseRequestDTO);
    }

    // ─── fallBackCreatePurchase (500) ──────────────────────────────────────────

    @Test
    @DisplayName("Debe devolver 503 SERVICE_UNAVAILABLE con mensaje de inestabilidad cuando el servicio devuelve 500")
    void shouldReturn503WithInstabilityMessageWhenServiceReturns500() {
        // Given
        FeignException.InternalServerError internalServerError =
                (FeignException.InternalServerError) FeignException.errorStatus(
                        "purchaseValidation",
                        feign.Response.builder()
                                .status(500)
                                .reason("Internal Server Error")
                                .headers(Collections.emptyMap())
                                .body(new byte[0])
                                .request(Request.create(Request.HttpMethod.POST, "/validate", Collections.emptyMap(), null, null, null))
                                .build()
                );

        // When
        ResponseEntity<CBResponseDTO> response = purchaseController.fallBackCreatePurchase(purchaseRequestDTO, internalServerError);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("El servicio de compras inestable, pruebe de nuevo");
    }

    // ─── fallBackCreatePurchase (CircuitBreaker abierto) ──────────────────────

    @Test
    @DisplayName("Debe devolver 503 SERVICE_UNAVAILABLE con mensaje de no disponible cuando el circuit breaker está abierto")
    void shouldReturn503WithUnavailableMessageWhenCircuitBreakerIsOpen() {
        // Given
        CallNotPermittedException callNotPermittedException = CallNotPermittedException
                .createCallNotPermittedException(
                        CircuitBreaker.ofDefaults("purchaseCircuitBreaker")
                );

        // When
        ResponseEntity<CBResponseDTO> response = purchaseController.fallBackCreatePurchase(purchaseRequestDTO, callNotPermittedException);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage())
                .isEqualTo("El servicio de compras no está disponible ahora mismo. Intenta más tarde.");
    }

    @Test
    @DisplayName("Debe devolver mensajes distintos según el tipo de fallo cuando se activa el fallback")
    void shouldReturnDifferentMessagesWhenFallbackIsTriggeredByDifferentExceptions() {
        // Given
        FeignException.InternalServerError internalServerError =
                (FeignException.InternalServerError) FeignException.errorStatus(
                        "purchaseValidation",
                        feign.Response.builder()
                                .status(500)
                                .reason("Internal Server Error")
                                .headers(Collections.emptyMap())
                                .body(new byte[0])
                                .request(Request.create(Request.HttpMethod.POST, "/validate", Collections.emptyMap(), null, null, null))
                                .build()
                );

        CallNotPermittedException callNotPermittedException = CallNotPermittedException
                .createCallNotPermittedException(
                        CircuitBreaker.ofDefaults("purchaseCircuitBreaker")
                );

        // When
        ResponseEntity<CBResponseDTO> response500 = purchaseController.fallBackCreatePurchase(purchaseRequestDTO, internalServerError);
        ResponseEntity<CBResponseDTO> responseCB = purchaseController.fallBackCreatePurchase(purchaseRequestDTO, callNotPermittedException);

        // Then
        assertThat(response500.getBody().getMessage())
                .isNotEqualTo(responseCB.getBody().getMessage());
        assertThat(response500.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(responseCB.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }
}

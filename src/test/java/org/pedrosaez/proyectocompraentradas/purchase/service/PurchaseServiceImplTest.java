package org.pedrosaez.proyectocompraentradas.purchase.service;

import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pedrosaez.proyectocompraentradas.feignclients.EventFeignClient;
import org.pedrosaez.proyectocompraentradas.feignclients.PurchaseValidationFeignClient;
import org.pedrosaez.proyectocompraentradas.purchase.controller.error.EventNotFoundException;
import org.pedrosaez.proyectocompraentradas.purchase.controller.error.PurchaseException;
import org.pedrosaez.proyectocompraentradas.purchase.model.adapter.PaymentRequestAdapter;
import org.pedrosaez.proyectocompraentradas.purchase.model.request.PaymentRequestDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.request.PurchaseRequestDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.CustomPurchaseResponseDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.EventDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.PurchaseResponseDTO;
import org.pedrosaez.proyectocompraentradas.purchase.repository.PurchaseRepository;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceImplTest {

        @Mock
        private EventFeignClient eventClient;

        @Mock
        private PurchaseValidationFeignClient purchaseValidationFeignClient;

        @Mock
        private PaymentRequestAdapter paymentRequestAdapter;

        @Mock
        private LucaBankingTokenService lucaBankingTokenService;

        @Mock
        private PurchaseRepository purchaseRepository;

        @InjectMocks
        private PurchaseServiceImpl purchaseService;

        private PurchaseRequestDTO purchaseRequest;
        private EventDTO eventDTO;
        private PaymentRequestDTO paymentRequestDTO;
        private PurchaseResponseDTO purchaseResponseDTO;

        @BeforeEach
        void setUp() {
            purchaseRequest = new PurchaseRequestDTO();
            purchaseRequest.setEventId(1L);

            eventDTO = new EventDTO();
            eventDTO.setId(1L);

            paymentRequestDTO = new PaymentRequestDTO();

            PurchaseResponseDTO.Info info = new PurchaseResponseDTO.Info();
            info.setConcepto("Concierto Rock");
            info.setCantidad("50.00");
            info.setNombreTitular("Pedro Saez");

            purchaseResponseDTO = new PurchaseResponseDTO();
            purchaseResponseDTO.setInfo(info);
            purchaseResponseDTO.setTimestamp("01/01/2025 10:00:00");
        }

        // ─── compraEntradas ────────────────────────────────────────────────────────

        @Test
        @DisplayName("Debe devolver CustomPurchaseResponseDTO con datos correctos cuando la compra es exitosa")
        void shouldReturnCustomPurchaseResponseDTOWhenCompraEntradasIsSuccessful() {
            // Given
            when(eventClient.getEventById(1L)).thenReturn(eventDTO);
            when(paymentRequestAdapter.toPaymentRequestDTO(purchaseRequest, eventDTO)).thenReturn(paymentRequestDTO);
            when(lucaBankingTokenService.getToken()).thenReturn("valid-token");
            when(purchaseValidationFeignClient.purchaseValidation(anyString(), any())).thenReturn(purchaseResponseDTO);

            // When
            CustomPurchaseResponseDTO result = purchaseService.compraEntradas(purchaseRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getEvento()).isEqualTo("Concierto Rock");
            assertThat(result.getCantidad()).isEqualTo("50.00");
            assertThat(result.getClientName()).isEqualTo("Pedro Saez");
            assertThat(result.getMensaje()).contains("Compra confirmada");
            verify(purchaseRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("Debe lanzar EventNotFoundException cuando el evento no existe")
        void shouldThrowEventNotFoundExceptionWhenEventDoesNotExist() {
            // Given
            when(eventClient.getEventById(1L)).thenReturn(null);

            // When & Then
            assertThatThrownBy(() -> purchaseService.compraEntradas(purchaseRequest))
                    .isInstanceOf(EventNotFoundException.class);

            verifyNoInteractions(purchaseValidationFeignClient);
        }

        // ─── Retry con token 401 ───────────────────────────────────────────────────

        @Test
        @DisplayName("Debe reintentar con nuevo token y devolver respuesta exitosa cuando el primer token está caducado")
        void shouldRetryWithNewTokenAndReturnSuccessWhenFirstTokenIsExpired() {
            // Given
            when(eventClient.getEventById(1L)).thenReturn(eventDTO);
            when(paymentRequestAdapter.toPaymentRequestDTO(any(), any())).thenReturn(paymentRequestDTO);
            when(lucaBankingTokenService.getToken()).thenReturn("expired-token", "new-token");

            FeignException.Unauthorized unauthorized = mockFeignException(FeignException.Unauthorized.class, 401);
            when(purchaseValidationFeignClient.purchaseValidation(anyString(), any()))
                    .thenThrow(unauthorized)
                    .thenReturn(purchaseResponseDTO);

            // When
            CustomPurchaseResponseDTO result = purchaseService.compraEntradas(purchaseRequest);

            // Then
            assertThat(result).isNotNull();
            verify(lucaBankingTokenService, times(1)).invalidateToken();
            verify(purchaseValidationFeignClient, times(2)).purchaseValidation(anyString(), any());
        }

        @Test
        @DisplayName("Debe lanzar PurchaseException 401.0001 cuando se agotan los 4 reintentos por token inválido")
        void shouldThrowPurchaseException401WhenAllRetriesAreExhausted() {
            // Given
            when(eventClient.getEventById(1L)).thenReturn(eventDTO);
            when(paymentRequestAdapter.toPaymentRequestDTO(any(), any())).thenReturn(paymentRequestDTO);
            when(lucaBankingTokenService.getToken()).thenReturn("token");

            FeignException.Unauthorized unauthorized = mockFeignException(FeignException.Unauthorized.class, 401);
            when(purchaseValidationFeignClient.purchaseValidation(anyString(), any()))
                    .thenThrow(unauthorized);

            // When & Then
            assertThatThrownBy(() -> purchaseService.compraEntradas(purchaseRequest))
                    .isInstanceOf(PurchaseException.class)
                    .extracting("code")
                    .isEqualTo("401.0001");

            verify(lucaBankingTokenService, times(4)).invalidateToken();
        }

        // ─── Errores 400 (BadRequest) ──────────────────────────────────────────────

        @Test
        @DisplayName("Debe lanzar PurchaseException 400.0001 cuando no hay fondos suficientes")
        void shouldThrowPurchaseException400001WhenInsufficientFunds() {
            // Given
            setupBadRequestScenario("400.0001.INSUFFICIENT_FUNDS");

            // When & Then
            assertThatThrownBy(() -> purchaseService.compraEntradas(purchaseRequest))
                    .isInstanceOf(PurchaseException.class)
                    .hasMessage("No hay fondos suficientes en la cuenta.")
                    .extracting("code").isEqualTo("400.0001");
        }

        @Test
        @DisplayName("Debe lanzar PurchaseException 400.0002 cuando no se encuentran los datos del cliente")
        void shouldThrowPurchaseException400002WhenCustomerDataNotFound() {
            // Given
            setupBadRequestScenario("400.0002.CUSTOMER_NOT_FOUND");

            // When & Then
            assertThatThrownBy(() -> purchaseService.compraEntradas(purchaseRequest))
                    .isInstanceOf(PurchaseException.class)
                    .hasMessage("No se encuentran los datos del cliente.")
                    .extracting("code").isEqualTo("400.0002");
        }

        @Test
        @DisplayName("Debe lanzar PurchaseException 400.0003 cuando el número de tarjeta no es válido")
        void shouldThrowPurchaseException400003WhenCardNumberIsInvalid() {
            // Given
            setupBadRequestScenario("400.0003.INVALID_CARD");

            // When & Then
            assertThatThrownBy(() -> purchaseService.compraEntradas(purchaseRequest))
                    .isInstanceOf(PurchaseException.class)
                    .hasMessage("El número de la tarjeta no es válido.")
                    .extracting("code").isEqualTo("400.0003");
        }

        @Test
        @DisplayName("Debe lanzar PurchaseException 400.0004 cuando el CVV no es válido")
        void shouldThrowPurchaseException400004WhenCvvIsInvalid() {
            // Given
            setupBadRequestScenario("400.0004.INVALID_CVV");

            // When & Then
            assertThatThrownBy(() -> purchaseService.compraEntradas(purchaseRequest))
                    .isInstanceOf(PurchaseException.class)
                    .hasMessage("El formato del CVV no es válido.")
                    .extracting("code").isEqualTo("400.0004");
        }

        @Test
        @DisplayName("Debe lanzar PurchaseException 400.0005 cuando el mes de caducidad no es correcto")
        void shouldThrowPurchaseException400005WhenExpiryMonthIsInvalid() {
            // Given
            setupBadRequestScenario("400.0005.INVALID_MONTH");

            // When & Then
            assertThatThrownBy(() -> purchaseService.compraEntradas(purchaseRequest))
                    .isInstanceOf(PurchaseException.class)
                    .hasMessage("El mes de caducidad no es correcto.")
                    .extracting("code").isEqualTo("400.0005");
        }

        @Test
        @DisplayName("Debe lanzar PurchaseException 400.0006 cuando el año de caducidad no es correcto")
        void shouldThrowPurchaseException400006WhenExpiryYearIsInvalid() {
            // Given
            setupBadRequestScenario("400.0006.INVALID_YEAR");

            // When & Then
            assertThatThrownBy(() -> purchaseService.compraEntradas(purchaseRequest))
                    .isInstanceOf(PurchaseException.class)
                    .hasMessage("El año de caducidad no es correcto.")
                    .extracting("code").isEqualTo("400.0006");
        }

        @Test
        @DisplayName("Debe lanzar PurchaseException 400.0007 cuando la tarjeta está caducada")
        void shouldThrowPurchaseException400007WhenCardIsExpired() {
            // Given
            setupBadRequestScenario("400.0007.EXPIRED_CARD");

            // When & Then
            assertThatThrownBy(() -> purchaseService.compraEntradas(purchaseRequest))
                    .isInstanceOf(PurchaseException.class)
                    .hasMessage("La tarjeta está caducada.")
                    .extracting("code").isEqualTo("400.0007");
        }

        @Test
        @DisplayName("Debe lanzar PurchaseException 400.0008 cuando el formato del nombre no es correcto")
        void shouldThrowPurchaseException400008WhenNameFormatIsInvalid() {
            // Given
            setupBadRequestScenario("400.0008.INVALID_NAME");

            // When & Then
            assertThatThrownBy(() -> purchaseService.compraEntradas(purchaseRequest))
                    .isInstanceOf(PurchaseException.class)
                    .hasMessage("El formato del nombre no es correcto.")
                    .extracting("code").isEqualTo("400.0008");
        }

        @Test
        @DisplayName("Debe lanzar PurchaseException por defecto cuando el código de error 400 es desconocido")
        void shouldThrowDefaultPurchaseExceptionWhenErrorCodeIsUnknown() {
            // Given
            setupBadRequestScenario("400.9999.UNKNOWN");

            // When & Then
            assertThatThrownBy(() -> purchaseService.compraEntradas(purchaseRequest))
                    .isInstanceOf(PurchaseException.class)
                    .hasMessage("Error de validación en la pasarela de pago.")
                    .extracting("code").isEqualTo("400.9999");
        }

        // ─── Helpers ──────────────────────────────────────────────────────────────

        private void setupBadRequestScenario(String errorCode) {
            when(eventClient.getEventById(1L)).thenReturn(eventDTO);
            when(paymentRequestAdapter.toPaymentRequestDTO(any(), any())).thenReturn(paymentRequestDTO);
            when(lucaBankingTokenService.getToken()).thenReturn("valid-token");

            String body = String.format("{\"error\":\"%s\"}", errorCode);
            FeignException.BadRequest badRequest = (FeignException.BadRequest) FeignException.errorStatus(
                    "purchaseValidation",
                    feign.Response.builder()
                            .status(400)
                            .reason("Bad Request")
                            .headers(Collections.emptyMap())
                            .body(body.getBytes())
                            .request(Request.create(Request.HttpMethod.POST, "/validate", Collections.emptyMap(), null, null, null))
                            .build()
            );

            when(purchaseValidationFeignClient.purchaseValidation(anyString(), any()))
                    .thenThrow(badRequest);
        }

        @SuppressWarnings("unchecked")
        private <T extends FeignException> T mockFeignException(Class<T> type, int status) {
            return (T) FeignException.errorStatus(
                    "purchaseValidation",
                    feign.Response.builder()
                            .status(status)
                            .reason("Error")
                            .headers(Collections.emptyMap())
                            .body(new byte[0])
                            .request(Request.create(Request.HttpMethod.POST, "/validate", Collections.emptyMap(), null, null, null))
                            .build()
            );
        }

}

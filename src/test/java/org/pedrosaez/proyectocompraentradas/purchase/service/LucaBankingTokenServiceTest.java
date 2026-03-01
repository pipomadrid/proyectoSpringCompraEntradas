package org.pedrosaez.proyectocompraentradas.purchase.service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pedrosaez.proyectocompraentradas.feignclients.PurchaseValidationFeignClient;
import org.pedrosaez.proyectocompraentradas.purchase.config.LucaBankingProperties;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.AuthResponseDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LucaBankingTokenServiceTest {

    @Mock
    private PurchaseValidationFeignClient purchaseValidationFeignClient;

    @Mock
    private LucaBankingProperties properties;

    @InjectMocks
    private LucaBankingTokenService lucaBankingTokenService;


    @Test
    @DisplayName("Debe obtener un nuevo token del cliente cuando no hay token en caché")
    void shouldFetchNewTokenFromClientWhenCacheIsEmpty() {
        // Given
        AuthResponseDTO authResponse = new AuthResponseDTO();
        authResponse.setToken("new-token");
        when(properties.getUser()).thenReturn("user");
        when(properties.getPassword()).thenReturn("password");
        when(purchaseValidationFeignClient.userValidation("user", "password")).thenReturn(authResponse);

        // When
        String token = lucaBankingTokenService.getToken();

        // Then
        assertThat(token).isEqualTo("new-token");
        verify(purchaseValidationFeignClient, times(1)).userValidation("user", "password");
    }

    @Test
    @DisplayName("Debe devolver el token en caché sin llamar al cliente cuando ya existe un token")
    void shouldReturnCachedTokenWithoutCallingClientWhenTokenAlreadyExists() {
        // Given
        AuthResponseDTO authResponse = new AuthResponseDTO();
        authResponse.setToken("cached-token");
        when(properties.getUser()).thenReturn("user");
        when(properties.getPassword()).thenReturn("password");
        when(purchaseValidationFeignClient.userValidation("user", "password")).thenReturn(authResponse);
        lucaBankingTokenService.getToken(); // primera llamada para poblar la caché

        // When
        String token = lucaBankingTokenService.getToken(); // segunda llamada

        // Then
        assertThat(token).isEqualTo("cached-token");
        verify(purchaseValidationFeignClient, times(1)).userValidation(anyString(), anyString()); // solo 1 llamada real
    }

    @Test
    @DisplayName("Debe llamar al cliente solo una vez cuando se llama getToken varias veces seguidas")
    void shouldCallClientOnlyOnceWhenGetTokenIsCalledMultipleTimes() {
        // Given
        AuthResponseDTO authResponse = new AuthResponseDTO();
        authResponse.setToken("token");
        when(properties.getUser()).thenReturn("user");
        when(properties.getPassword()).thenReturn("password");
        when(purchaseValidationFeignClient.userValidation("user", "password")).thenReturn(authResponse);

        // When
        lucaBankingTokenService.getToken();
        lucaBankingTokenService.getToken();
        lucaBankingTokenService.getToken();

        // Then
        verify(purchaseValidationFeignClient, times(1)).userValidation("user", "password");
    }

    // ─── invalidateToken ──────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe obtener un nuevo token del cliente cuando se llama getToken tras invalidar el token")
    void shouldFetchNewTokenFromClientWhenGetTokenIsCalledAfterInvalidation() {
        // Given
        AuthResponseDTO firstResponse = new AuthResponseDTO();
        firstResponse.setToken("first-token");

        AuthResponseDTO secondResponse = new AuthResponseDTO();
        secondResponse.setToken("second-token");

        when(properties.getUser()).thenReturn("user");
        when(properties.getPassword()).thenReturn("password");
        when(purchaseValidationFeignClient.userValidation("user", "password"))
                .thenReturn(firstResponse)
                .thenReturn(secondResponse);

        lucaBankingTokenService.getToken(); // pobla la caché con first-token

        // When
        lucaBankingTokenService.invalidateToken();
        String tokenAfterInvalidation = lucaBankingTokenService.getToken();

        // Then
        assertThat(tokenAfterInvalidation).isEqualTo("second-token");
        verify(purchaseValidationFeignClient, times(2)).userValidation("user", "password");
    }

    @Test
    @DisplayName("Debe devolver null cuando se invalida el token sin haber obtenido uno previamente")
    void shouldLeaveTokenNullWhenInvalidateIsCalledWithNoExistingToken() {
        // Given — no se ha llamado a getToken previamente

        // When
        lucaBankingTokenService.invalidateToken();
        // no lanzamos excepción, simplemente se queda en null

        // Then
        verifyNoInteractions(purchaseValidationFeignClient);
    }
}

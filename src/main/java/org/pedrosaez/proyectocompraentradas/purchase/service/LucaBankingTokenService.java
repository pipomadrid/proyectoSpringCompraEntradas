package org.pedrosaez.proyectocompraentradas.purchase.service;

import org.pedrosaez.proyectocompraentradas.feignclients.PurchaseValidationFeignClient;
import org.pedrosaez.proyectocompraentradas.purchase.config.LucaBankingProperties;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.AuthResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LucaBankingTokenService {

    private PurchaseValidationFeignClient purchaseValidationFeignClient;

    private final LucaBankingProperties properties;

    @Autowired
    public LucaBankingTokenService(LucaBankingProperties properties, PurchaseValidationFeignClient purchaseValidationFeignClient) {
        this.properties = properties;
        this.purchaseValidationFeignClient = purchaseValidationFeignClient;
    }

    private String cachedToken;

    public LucaBankingTokenService(LucaBankingProperties properties) {
        this.properties = properties;
    }

    public synchronized String getToken() {

        if (cachedToken == null) {
            AuthResponseDTO response = purchaseValidationFeignClient.userValidation(
                    properties.getUser(),
                    properties.getPassword()
            );
            cachedToken = response.getToken();
        }

        return cachedToken;
    }

    public synchronized void invalidateToken() {
        cachedToken = null;
    }
}

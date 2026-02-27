package org.pedrosaez.proyectocompraentradas.purchase.controller;


import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.pedrosaez.proyectocompraentradas.purchase.model.request.PurchaseRequestDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.CBResponse;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.CustomPurchaseResponseDTO;
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

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    private PurchaseService purchaseService;

    @CircuitBreaker(name = "purchaseCircuitBreaker", fallbackMethod = "fallBackCreatePurchase")
    @PostMapping
    public ResponseEntity<?> createPurchase(@Valid @RequestBody PurchaseRequestDTO purchaseDTO){

        logger.info("------ createPurchase {} (POST)",purchaseDTO.getId());
        CustomPurchaseResponseDTO result =  purchaseService.compraEntradas(purchaseDTO);
        logger.info("------ Purchase created successfully");

        return  ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    private ResponseEntity<CBResponse> serviceUnavailableResponse(String message) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new CBResponse(
                        message,
                        HttpStatus.SERVICE_UNAVAILABLE
                ));
    }

    public ResponseEntity<CBResponse> fallBackCreatePurchase(PurchaseRequestDTO purchaseDTO, FeignException.InternalServerError e){
        String message = "El servicio de compras inestable, pruebe de nuevo";
        return serviceUnavailableResponse(message);
    }

    public ResponseEntity<CBResponse> fallBackCreatePurchase(PurchaseRequestDTO purchaseDTO, CallNotPermittedException e){
        String message = "El servicio de compras no está disponible ahora mismo. Intenta más tarde.";
        return serviceUnavailableResponse(message);
    }
}

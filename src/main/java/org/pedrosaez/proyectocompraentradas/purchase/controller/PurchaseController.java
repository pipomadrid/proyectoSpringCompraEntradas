package org.pedrosaez.proyectocompraentradas.purchase.controller;


import jakarta.validation.Valid;
import org.pedrosaez.proyectocompraentradas.purchase.model.Purchase;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.PurchaseDTO;
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


    @PostMapping
    public ResponseEntity<?> createPurchase(@Valid @RequestBody PurchaseDTO purchaseDTO){

        logger.info("------ createPurchase {} (POST)",purchaseDTO.getId());
        Purchase result =  purchaseService.compraEntradas(purchaseDTO);
        logger.info("------ Purchase {} created successfully", result.getId());

        return  ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}

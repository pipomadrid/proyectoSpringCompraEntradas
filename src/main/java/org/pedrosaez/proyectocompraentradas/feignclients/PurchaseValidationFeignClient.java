package org.pedrosaez.proyectocompraentradas.feignclients;


import org.pedrosaez.proyectocompraentradas.purchase.model.request.PaymentRequestDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.AuthResponseDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.PurchaseResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "banco", url= "http://lucabanking.us-east-1.elasticbeanstalk.com")
public interface PurchaseValidationFeignClient {


    // Compra sin validacion
    @PostMapping(value = "/pasarela/compra", consumes = "application/json")
    PurchaseResponseDTO purchase( @RequestBody PaymentRequestDTO request);

    // Validacion de usuario
    @PostMapping(value ="/pasarela/validaruser",consumes = "application/x-www-form-urlencoded")
    AuthResponseDTO userValidation(@RequestParam("user") String user,@RequestParam("password") String password);

    // Compra con validacion
    @PostMapping("/pasarela/validacion")
    PurchaseResponseDTO purchaseValidation(@RequestHeader("Authorization") String token, @RequestBody PaymentRequestDTO request);



}

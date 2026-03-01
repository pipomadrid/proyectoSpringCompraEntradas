package org.pedrosaez.proyectocompraentradas.purchase.service;

import feign.FeignException;
import org.pedrosaez.proyectocompraentradas.feignclients.EventFeignClient;
import org.pedrosaez.proyectocompraentradas.feignclients.PurchaseValidationFeignClient;
import org.pedrosaez.proyectocompraentradas.purchase.controller.error.EventNotFoundException;
import org.pedrosaez.proyectocompraentradas.purchase.controller.error.PurchaseException;
import org.pedrosaez.proyectocompraentradas.purchase.model.Purchase;
import org.pedrosaez.proyectocompraentradas.purchase.model.adapter.PaymentRequestAdapter;
import org.pedrosaez.proyectocompraentradas.purchase.model.request.PaymentRequestDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.request.PurchaseRequestDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.CustomPurchaseResponseDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.EventDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.PurchaseResponseDTO;
import org.pedrosaez.proyectocompraentradas.purchase.repository.PurchaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    @Autowired
    private EventFeignClient eventClient;

    @Autowired
    private PurchaseValidationFeignClient purchaseValidationFeignClient;

    @Autowired
    private PaymentRequestAdapter paymentRequestAdapter;

    @Autowired
    private LucaBankingTokenService lucaBankingTokenService;

    @Autowired
    private PurchaseRepository purchaseRepository;


    @Override
    public CustomPurchaseResponseDTO compraEntradas(PurchaseRequestDTO request) {

        EventDTO event = eventClient.getEventById(request.getEventId());

        if (event == null) {
            throw new EventNotFoundException(request.getEventId());
        }
        logger.info("------ event whit id {} valid ()", event.getId());

        PaymentRequestDTO paymentRequestDTO = paymentRequestAdapter.toPaymentRequestDTO(request, event);

        logger.info("------ payment request {} ()", paymentRequestDTO);


        PurchaseResponseDTO purchaseResponseDTO = executePurchaseValidation(paymentRequestDTO);


        Purchase purchase = new Purchase();
        purchase.setEventId(event.getId());
        purchase.setCustomerName(purchaseResponseDTO.getInfo().getNombreTitular());
        purchase.setAmount(new BigDecimal(purchaseResponseDTO.getInfo().getCantidad()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime purchaseTime = LocalDateTime.parse(purchaseResponseDTO.getTimestamp(), formatter);
        purchase.setPurchaseDate(purchaseTime);
        purchaseRepository.save(purchase);

        return buildCustomResponse(purchaseResponseDTO);

    }

    private PurchaseResponseDTO executePurchaseValidation(PaymentRequestDTO paymentRequestDTO) {
        int maxRetries = 4;
        int attempt = 0;

        while (attempt < maxRetries) {
            String token = lucaBankingTokenService.getToken();
            try {
                return purchaseValidationFeignClient.purchaseValidation(token, paymentRequestDTO);
            } catch (FeignException.Unauthorized e) {
                lucaBankingTokenService.invalidateToken();
                attempt++;
            } catch (FeignException.BadRequest ex) {
                handleFeignBadRequest(ex);
            }
        }
        throw new PurchaseException("401.0001", "No autorizado: token inválido o caducado");
    }


    private CustomPurchaseResponseDTO buildCustomResponse(PurchaseResponseDTO purchaseResponseDTO) {

        String eventName = purchaseResponseDTO.getInfo().getConcepto();
        String amount = purchaseResponseDTO.getInfo().getCantidad();
        String timestamp = purchaseResponseDTO.getTimestamp();
        String customer = purchaseResponseDTO.getInfo().getNombreTitular();

        String customMessage = "Compra confirmada, " + "🎉 Disfruta del Evento 🎉";


        return new CustomPurchaseResponseDTO(customMessage,eventName,amount,timestamp,customer);
    }


    private void handleFeignBadRequest(FeignException.BadRequest ex) {

            String body = ex.contentUTF8();
            ObjectMapper mapper = new ObjectMapper();
            PurchaseResponseDTO errorResponse = mapper.readValue(body, PurchaseResponseDTO.class);
            String code = errorResponse.getError().split("\\.")[0] + "." + errorResponse.getError().split("\\.")[1];
            handleClientErrors(code);
    }


    private void handleClientErrors(String errorCode) {

        switch (errorCode) {
            case "400.0001":
                throw new PurchaseException(errorCode,
                        "No hay fondos suficientes en la cuenta.");

            case "400.0002":
                throw new PurchaseException(errorCode,
                        "No se encuentran los datos del cliente.");

            case "400.0003":
                throw new PurchaseException(errorCode,
                        "El número de la tarjeta no es válido.");

            case "400.0004":
                throw new PurchaseException(errorCode,
                        "El formato del CVV no es válido.");

            case "400.0005":
                throw new PurchaseException(errorCode,
                        "El mes de caducidad no es correcto.");

            case "400.0006":
                throw new PurchaseException(errorCode,
                        "El año de caducidad no es correcto.");

            case "400.0007":
                throw new PurchaseException(errorCode,
                        "La tarjeta está caducada.");

            case "400.0008":
                throw new PurchaseException(errorCode,
                        "El formato del nombre no es correcto.");

            default:
                throw new PurchaseException(errorCode,
                        "Error de validación en la pasarela de pago.");
        }

    }

}

package org.pedrosaez.proyectocompraentradas.purchase.model.adapter;


import org.jspecify.annotations.NonNull;
import org.pedrosaez.proyectocompraentradas.purchase.model.request.PaymentRequestDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.request.PurchaseRequestDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.BankCardDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.EventDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class PaymentRequestAdapter {

    public PaymentRequestDTO toPaymentRequestDTO(PurchaseRequestDTO request, EventDTO event) {
        BankCardDTO bankCardDTO = request.getBankCardDTO();

        PaymentRequestDTO  paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setNombreTitular(bankCardDTO.getOwnerName());
        paymentRequestDTO.setNumeroTarjeta(bankCardDTO.getCardNumber());
        paymentRequestDTO.setConcepto(event.getName());
        paymentRequestDTO.setMesCaducidad(bankCardDTO.getExpiryMonth().toString());
        paymentRequestDTO.setYearCaducidad(bankCardDTO.getExpiryYear().toString());
        paymentRequestDTO.setEmisor("Lucatic");
        paymentRequestDTO.setCvv(bankCardDTO.getCvv());

        BigDecimal min = event.getMinPrice();
        BigDecimal max = event.getMaxPrice();

        BigDecimal random = min.add(
                BigDecimal.valueOf(ThreadLocalRandom.current()
                                .nextDouble())
                        .multiply(max.subtract(min))
        );

        random = random.setScale(2, RoundingMode.HALF_UP);
        paymentRequestDTO.setCantidad(random.toPlainString());

        return paymentRequestDTO;
    }
}

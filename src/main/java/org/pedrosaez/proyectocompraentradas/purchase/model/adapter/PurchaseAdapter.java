package org.pedrosaez.proyectocompraentradas.purchase.model.adapter;

import org.pedrosaez.proyectocompraentradas.purchase.model.BankCard;
import org.pedrosaez.proyectocompraentradas.purchase.model.Purchase;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.BankCardDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.PurchaseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseAdapter {



    public PurchaseDTO convertToDTO(Purchase purchase) {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setEmail(purchase.getEmail());
        dto.setId(purchase.getId());
        dto.setEventId(purchase.getEventId());

        BankCard bankCard = purchase.getBankCard();

        BankCardDTO bankCardDTO = new BankCardDTO();
        bankCardDTO.setCardNumber(bankCard.getCardNumber());
        bankCardDTO.setCvv(bankCard.getCvv());
        bankCardDTO.setExpiryMonth(bankCard.getExpiryMonth());
        bankCardDTO.setExpiryYear(bankCard.getExpiryYear());
        bankCardDTO.setOwnerName(bankCard.getOwnerName());

        dto.setBankCard(bankCardDTO);

        return dto;
    }

    public List<PurchaseDTO> convertToDTO(List<Purchase> events) {
        return events.stream().map(this::convertToDTO).collect(Collectors.toList());

    }

    public Purchase convertToEntity(PurchaseDTO dto) {
        Purchase purchase = new Purchase();
        purchase.setEmail(dto.getEmail());
        purchase.setId(dto.getId());
        purchase.setEventId(dto.getEventId());

        BankCardDTO bankCardDTO = dto.getBankCard();

        BankCard bankCard = new BankCard();
        bankCard.setOwnerName(bankCardDTO.getOwnerName());
        bankCard.setCardNumber(bankCardDTO.getCardNumber());
        bankCard.setExpiryMonth(bankCardDTO.getExpiryMonth());
        bankCard.setExpiryYear(bankCardDTO.getExpiryYear());
        bankCard.setCvv(bankCardDTO.getCvv());

        purchase.setBankCard(bankCard);

        return purchase;
    }

}

package org.pedrosaez.proyectocompraentradas.purchase.service;

import org.pedrosaez.proyectocompraentradas.feignclients.EventFeignClient;
import org.pedrosaez.proyectocompraentradas.purchase.controller.PurchaseController;
import org.pedrosaez.proyectocompraentradas.purchase.controller.error.EventNotFoundException;
import org.pedrosaez.proyectocompraentradas.purchase.model.Purchase;
import org.pedrosaez.proyectocompraentradas.purchase.model.adapter.PurchaseAdapter;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.EventDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.PurchaseDTO;
import org.pedrosaez.proyectocompraentradas.purchase.repository.PurchaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServiceImpl implements PurchaseService{

    private static final Logger logger = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    @Autowired
    private EventFeignClient eventClient;

    @Autowired
    private PurchaseAdapter purchaseAdapter;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public Purchase compraEntradas(PurchaseDTO request) {
        // Validamos el evento
        EventDTO event = eventClient.getEventById(request.getEventId());

        if(event == null){
            throw new EventNotFoundException(request.getEventId());
        }

        Purchase purchase = purchaseAdapter.convertToEntity(request);

        return purchaseRepository.save(purchase);
    }
}

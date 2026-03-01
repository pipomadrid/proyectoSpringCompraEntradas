package org.pedrosaez.proyectocompraentradas.purchase.service;

import org.pedrosaez.proyectocompraentradas.purchase.model.request.PurchaseRequestDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.CustomPurchaseResponseDTO;

public interface PurchaseService {

    public CustomPurchaseResponseDTO compraEntradas(PurchaseRequestDTO request);
}

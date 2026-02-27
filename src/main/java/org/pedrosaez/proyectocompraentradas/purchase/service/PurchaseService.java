package org.pedrosaez.proyectocompraentradas.purchase.service;

import org.pedrosaez.proyectocompraentradas.purchase.model.request.PurchaseRequestDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.CustomPurchaseResponseDTO;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.PurchaseResponseDTO;

public interface PurchaseService {

    public CustomPurchaseResponseDTO compraEntradas(PurchaseRequestDTO request);
}

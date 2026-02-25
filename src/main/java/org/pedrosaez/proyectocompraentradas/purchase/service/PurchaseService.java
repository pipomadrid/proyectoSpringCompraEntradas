package org.pedrosaez.proyectocompraentradas.purchase.service;

import org.pedrosaez.proyectocompraentradas.purchase.model.Purchase;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.PurchaseDTO;

public interface PurchaseService {

    public Purchase compraEntradas(PurchaseDTO request);
}

package org.pedrosaez.proyectocompraentradas.purchase.repository;

import org.pedrosaez.proyectocompraentradas.purchase.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}

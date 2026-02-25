package org.pedrosaez.proyectocompraentradas.purchase.repository;


import org.pedrosaez.proyectocompraentradas.purchase.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}

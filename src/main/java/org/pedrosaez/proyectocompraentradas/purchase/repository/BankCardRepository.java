package org.pedrosaez.proyectocompraentradas.purchase.repository;

import org.pedrosaez.proyectocompraentradas.purchase.model.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankCardRepository extends JpaRepository<BankCard, Long> {
}

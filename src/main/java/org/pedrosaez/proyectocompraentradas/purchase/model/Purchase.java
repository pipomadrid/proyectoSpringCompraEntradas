package org.pedrosaez.proyectocompraentradas.purchase.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Schema(description="Entidad que representa un proceso de compra")
@Entity
@Table(name = "purchases")
public class Purchase {

    @Schema(description="ID único de la compra",example="1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description="ID del evento asociado a la compra",example="1")
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Schema(description="Nombre del usuario que realiza la compra",example="Manolito")
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Schema(description="Precios de la compra",example="250,50")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Schema(description="Fecha de la compra",example="2026-02-27 10:56:10.000000")
    @Column(name = "purchase_date", nullable = false)
    private LocalDateTime purchaseDate;

    public Purchase() {

    }
    public Long getId() {
        return id;
    }

    public Purchase(Long id, Long eventId, String customerName, BigDecimal amount, LocalDateTime purchaseDate) {
        this.id = id;
        this.eventId = eventId;
        this.customerName = customerName;
        this.amount = amount;
        this.purchaseDate = purchaseDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}

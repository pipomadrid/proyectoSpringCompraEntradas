package org.pedrosaez.proyectocompraentradas.purchase.model.response;

import java.io.Serializable;
import java.math.BigDecimal;

public class EventDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;
    private String name;
    private String description;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
}

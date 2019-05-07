package com.github.middleware.fsm.builder.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/13
 */
public class Order implements Serializable {
    private int id;
    private String name;
    private BigDecimal price;

    public Order() {
    }

    public Order(int id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

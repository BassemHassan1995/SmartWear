package com.gp.smart.wear.Entities;

import java.util.List;

/**
 * Created by basse on 22-Jun-17.
 */

public class Order {
    private String date ;
    private String status;
    private String number;
    private String owner;
    private String price;
    private List<String> orderWatches_IDs;

    public Order() {
    }

    public Order(String date, String status, String number, String owner, String price, List<String> orderWatches_IDs) {
        this.date = date;
        this.status = status;
        this.number = number;
        this.owner = owner;
        this.price = price;
        this.orderWatches_IDs = orderWatches_IDs;
    }

    public List<String> getOrderWatches_IDs() {
        return orderWatches_IDs;
    }

    public void setOrderWatches_IDs(List<String> orderWatches_IDs) {
        this.orderWatches_IDs = orderWatches_IDs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

package com.gp.smart.wear.Entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by basse on 19-Jun-17.
 */

public class User implements Serializable {
    public String first_name;
    public String last_name;
    public String email;
    public String mobile_number;
    private Cart cart;
    public List<Order> orders;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String first_name, String last_name, String email, String mobile_number, Cart cart)  {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.mobile_number = mobile_number;
        this.cart = cart;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }
}

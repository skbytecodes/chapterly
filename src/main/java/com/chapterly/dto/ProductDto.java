package com.chapterly.dto;

import com.chapterly.entity.ShippingAddress;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ProductDto implements Serializable {
    private String amount;
    private List<Map<String, String>> notes;

    private ShippingAddress shippingAddress;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<Map<String, String>> getNotes() {
        return notes;
    }

    public void setNotes(List<Map<String, String>> notes) {
        this.notes = notes;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}


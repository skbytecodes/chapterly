package com.chapterly.dto;

import com.chapterly.entity.OrderedItem;

import java.io.Serializable;
import java.util.List;

public class OrdersResponseDto implements Serializable {

    private String orderId;
    private String orderPaymentTimeStamp;
    private List<OrderResponse> orderedItems;
    private String totalOrderAmount;

    private String deliveryTimeStamp;

    private String cardLastDigits;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderPaymentTimeStamp() {
        return orderPaymentTimeStamp;
    }

    public void setOrderPaymentTimeStamp(String orderPaymentTimeStamp) {
        this.orderPaymentTimeStamp = orderPaymentTimeStamp;
    }

    public List<OrderResponse> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<OrderResponse> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public String getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(String totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public String getDeliveryTimeStamp() {
        return deliveryTimeStamp;
    }

    public void setDeliveryTimeStamp(String deliveryTimeStamp) {
        this.deliveryTimeStamp = deliveryTimeStamp;
    }

    public String getCardLastDigits() {
        return cardLastDigits;
    }

    public void setCardLastDigits(String cardLastDigits) {
        this.cardLastDigits = cardLastDigits;
    }
}

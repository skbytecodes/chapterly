package com.chapterly.dto;

import java.io.Serializable;
import java.util.Objects;

public class OrderResponse implements Serializable {

    private String order_id;
    private String order_date;
    private String razorpay_order_id;
    private String user_id;
    private String quantity;
    private String status;
    private String total_amount;
    private String book_title;
    private String book_price;
    private String book_image;
    private String author_name;
    private String payment_date;
    private String payment_status;

    private String cardLastFourDigits;

    private String format;

    private String deliveryDate;


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getRazorpay_order_id() {
        return razorpay_order_id;
    }

    public void setRazorpay_order_id(String razorpay_order_id) {
        this.razorpay_order_id = razorpay_order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_price() {
        return book_price;
    }

    public void setBook_price(String book_price) {
        this.book_price = book_price;
    }

    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCardLastFourDigits() {
        return cardLastFourDigits;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setCardLastFourDigits(String cardLastFourDigits) {
        this.cardLastFourDigits = cardLastFourDigits;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Same object reference
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Not the same class
        }
        OrderResponse other = (OrderResponse) obj; // Cast to OrderResponse

        // Compare relevant fields for value equality
        boolean areEqual = Objects.equals(this.order_id, other.order_id);
        return areEqual;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.order_id != null ? this.order_id.hashCode() : 0);
        return result;
    }


}

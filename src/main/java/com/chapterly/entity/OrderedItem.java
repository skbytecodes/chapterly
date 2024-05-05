package com.chapterly.entity;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class OrderedItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long OrderedItemId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "order_id",
            referencedColumnName = "orderId",
            nullable = false
    )
    private Order order;

    @ManyToOne
    @JoinColumn(
            name = "book_id",
            referencedColumnName = "bookId"
    )
    private Book book;
    private int itemCount;

    public Long getOrderedItemId() {
        return OrderedItemId;
    }

    public void setOrderedItemId(Long orderedItemId) {
        OrderedItemId = orderedItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    @Override
    public String toString() {
        return "OrderedItem{" +
                "OrderedItemId=" + OrderedItemId +
                ", order=" + order +
                ", book=" + book +
                ", itemCount=" + itemCount +
                '}';
    }
}

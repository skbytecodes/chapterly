package com.chapterly.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Cart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    @JsonManagedReference
    private List<CartItem> cartItems;

    @OneToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId"
    )
    private User user;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

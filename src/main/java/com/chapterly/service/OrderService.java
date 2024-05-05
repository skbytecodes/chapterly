package com.chapterly.service;

import com.chapterly.dto.OrdersResponseDto;
import com.chapterly.dto.ProductDto;
import com.razorpay.Order;

import java.security.Principal;
import java.util.List;

public interface OrderService {
    Order createOrder(ProductDto productDto);
    com.chapterly.entity.Order getOrderByRazorPayOrderId(String orderId);
    List<OrdersResponseDto> getAllOrdersByUser(Principal principal) throws Exception;
}

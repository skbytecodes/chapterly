package com.chapterly.controller;

import com.chapterly.dto.OrdersResponseDto;
import com.chapterly.dto.ProductDto;
import com.chapterly.service.OrderService;
import com.razorpay.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    Logger logger = LoggerFactory.getLogger("OrderController");
    /***
     * create an order on razorpay
     * @param product is product with amount
     * @return Response with message
     */
    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody ProductDto product) {
        if(product != null){
            Order order = orderService.createOrder(product);
            if(order != null)
                return new ResponseEntity<String>(order.toString(), HttpStatus.OK);
            else
                return new ResponseEntity<String>("AMOUNT NOT AVAILABLE", HttpStatus.BAD_REQUEST);
        }else {
            logger.error("PRODUCT IS NULL");
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Retrieves all orders for the authenticated user.
     * @param principal The authenticated user's principal.
     * @return A ResponseEntity containing a list of OrderDto objects representing the user's orders, or a ResponseEntity with an error message if an exception occurs.
     * @throws IllegalStateException If the user is not found.
     */
    @GetMapping("/userOrders")
    public ResponseEntity<?> getOrdersByUser(Principal principal){
        try{
            List<OrdersResponseDto> orders = orderService.getAllOrdersByUser(principal);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }catch(IllegalStateException e){
            return new ResponseEntity<>("NOT FOUND", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

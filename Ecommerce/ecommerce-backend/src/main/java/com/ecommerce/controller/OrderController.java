package com.ecommerce.controller;

import com.ecommerce.entity.Order;
import com.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(Authentication authentication,
                                            @RequestParam String shippingAddress) {

        Order order = orderService.placeOrder(
                authentication.getName(),
                shippingAddress
        );

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(Authentication authentication) {
        return ResponseEntity.ok(
                orderService.getUserOrders(authentication.getName())
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(Authentication authentication,
                                          @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderById(authentication.getName(), orderId)
        );
    }
}
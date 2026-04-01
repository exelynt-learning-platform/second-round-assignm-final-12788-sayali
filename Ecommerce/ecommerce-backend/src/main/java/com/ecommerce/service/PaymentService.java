package com.ecommerce.service;

import com.ecommerce.entity.Order;
import com.ecommerce.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Autowired
    private OrderRepository orderRepository;

    public String processPayment(Long orderId) throws StripeException {

        Stripe.apiKey = stripeSecretKey;

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (order.getTotalPrice() * 100))
                .setCurrency("inr")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        if ("succeeded".equals(paymentIntent.getStatus())) {
            order.setPaymentStatus("PAID");
            order.setOrderStatus("CONFIRMED");
        } else {
            order.setPaymentStatus("FAILED");
        }

        orderRepository.save(order);

        return paymentIntent.getClientSecret();
    }
}
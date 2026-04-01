package com.ecommerce.controller;

import com.ecommerce.entity.CartItem;
import com.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(Authentication authentication) {
        return ResponseEntity.ok(
                cartService.getUserCart(authentication.getName())
        );
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(Authentication authentication,
                                              @RequestParam Long productId,
                                              @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                cartService.addToCart(authentication.getName(), productId, quantity)
        );
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItem> updateCart(Authentication authentication,
                                               @PathVariable Long cartItemId,
                                               @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                cartService.updateCartItem(authentication.getName(), cartItemId, quantity)
        );
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> removeFromCart(Authentication authentication,
                                                 @PathVariable Long cartItemId) {

        cartService.removeCartItem(authentication.getName(), cartItemId);
        return ResponseEntity.ok("Cart item removed successfully");
    }
}
package com.freshmart.controller;

import com.freshmart.model.CartItem;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    public static final Map<String, List<CartItem>> carts = new ConcurrentHashMap<>();
    private final AtomicLong cartCounter = new AtomicLong(0);

    @PostMapping("/")
    public String createCart() {
        String cartId = UUID.randomUUID().toString();
        carts.put(cartId, new ArrayList<>());
        return cartId;
    }

    @GetMapping("/{cartId}")
    public List<CartItem> getCart(@PathVariable String cartId) {
        return carts.getOrDefault(cartId, Collections.emptyList());
    }

    @PostMapping("/{cartId}/items")
    public void addCartItem(@PathVariable String cartId, @RequestBody CartItem item) {
        carts.computeIfAbsent(cartId, k -> new ArrayList<>()).add(item);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public void removeCartItem(@PathVariable String cartId, @PathVariable Long productId) {
        List<CartItem> cartItems = carts.get(cartId);
        if (cartItems != null) {
            cartItems.removeIf(item -> item.getProductId().equals(productId));
        }
    }

    @GetMapping("/{cartId}/total")
    public BigDecimal getCartTotal(@PathVariable String cartId) {
        List<CartItem> cartItems = carts.get(cartId);
        if (cartItems == null || cartItems.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return cartItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
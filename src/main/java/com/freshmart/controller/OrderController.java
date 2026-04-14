package com.freshmart.controller;

import com.freshmart.model.CartItem;
import com.freshmart.model.Order;
import com.freshmart.model.OrderItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    // In-memory storage for orders
    private static final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private static final AtomicLong orderIdSeq = new AtomicLong(1);

    // DTO for checkout request
    public static class CheckoutRequest {
        private String cartId;
        private String customerName;
        private String customerEmail;
        private String customerAddress;

        // Getters and setters
        public String getCartId() { return cartId; }
        public void setCartId(String cartId) { this.cartId = cartId; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public String getCustomerEmail() { return customerEmail; }
        public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
        public String getCustomerAddress() { return customerAddress; }
        public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }
    }

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@RequestBody CheckoutRequest request) {
        // Retrieve cart items from CartController static map
        List<CartItem> cartItems = CartController.carts.getOrDefault(request.getCartId(), new ArrayList<>());
        // Calculate total
        BigDecimal total = cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create Order
        Order order = new Order();
        long orderId = orderIdSeq.getAndIncrement();
        order.setId(orderId);
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setTotal(total);
        // status defaults to PENDING, createdAt set in constructor

        // Create OrderItems (not persisted, just for completeness)
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem(orderId, ci.getProductName(), ci.getQuantity(), ci.getPrice());
            orderItems.add(oi);
        }
        // In a real app you'd persist orderItems; here we just ignore them.

        // Store order
        orders.put(orderId, order);

        // Clear cart
        CartController.carts.remove(request.getCartId());

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Order order = orders.get(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }
}

package com.freshmart.controller;

import com.freshmart.model.CartItem;
import com.freshmart.model.Order;
import com.freshmart.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private CartController cartController;

    // In‑memory storage for orders and order items (simple for this task)
    private static final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private static final Map<Long, List<OrderItem>> orderItems = new ConcurrentHashMap<>();
    private static final AtomicLong orderIdSeq = new AtomicLong(1);

    public static class CheckoutRequest {
        public String cartId;
        public String customerName;
        public String customerEmail;
        public String customerAddress;
    }

    @Autowired
    private com.freshmart.repository.ProductRepository productRepository;

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody CheckoutRequest req) {
        // Retrieve cart items
        List<CartItem> items = cartController.getCart(req.cartId);
        if (items == null) {
            return ResponseEntity.badRequest().build();
        }
        // Calculate total
        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // Create order
        Order order = new Order(req.customerName, req.customerEmail, req.customerAddress, total);
        long orderId = orderIdSeq.getAndIncrement();
        order.setId(orderId);
        orders.put(orderId, order);
        // Create order items and decrement stock
        List<OrderItem> oItems = items.stream()
                .map(i -> {
                    // decrement product stock
                    productRepository.findById(i.getProductId()).ifPresent(p -> {
                        int newStock = p.getStockQuantity() - i.getQuantity();
                        p.setStockQuantity(Math.max(newStock, 0));
                        productRepository.save(p);
                    });
                    return new OrderItem(orderId, i.getProductName(), i.getQuantity(), i.getPrice());
                })
                .toList();
        orderItems.put(orderId, oItems);
        // Clear cart
        cartController.clearCart(req.cartId);
        CheckoutResponse resp = new CheckoutResponse(order, oItems);
        return ResponseEntity.status(201).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckoutResponse> getOrder(@PathVariable Long id) {
        Order order = orders.get(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        List<OrderItem> items = orderItems.getOrDefault(id, List.of());
        CheckoutResponse resp = new CheckoutResponse(order, items);
        return ResponseEntity.ok(resp);
    }

    public static class CheckoutResponse {
        public Order order;
        public List<OrderItem> items;
        public CheckoutResponse(Order order, List<OrderItem> items) {
            this.order = order;
            this.items = items;
        }
    }

    
}

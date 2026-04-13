# FreshMart Grocery Ecommerce Platform - Product Requirements

## Overview
FreshMart is an online grocery store serving a local community. Customers browse products by category, add items to a shopping cart, and checkout. The platform uses an in-memory H2 database (no external DB needed).

## Core Features

### 1. Product Catalog
- Products have: name, description, price (BigDecimal), category, imageUrl, stockQuantity
- Categories: PRODUCE, DAIRY, BAKERY, MEAT, BEVERAGES, SNACKS, FROZEN, HOUSEHOLD
- REST API: GET /api/products (list all, filter by category), GET /api/products/{id}, POST /api/products (admin)
- Products should be seeded with sample data on startup (at least 3 per category)

### 2. Shopping Cart
- Cart is session-based (in-memory, keyed by a cartId UUID)
- REST API: POST /api/cart (create new cart, returns cartId), GET /api/cart/{cartId}, POST /api/cart/{cartId}/items (add item with productId and quantity), DELETE /api/cart/{cartId}/items/{productId}, GET /api/cart/{cartId}/total
- Cart total calculated from product prices * quantities

### 3. Checkout
- POST /api/checkout with cartId and customer info (name, email, address)
- Creates an Order with status PENDING
- Decrements product stock quantities
- Returns order confirmation with orderId and total

### 4. Order History
- GET /api/orders/{orderId} - get order details
- Order contains: customer info, line items (product name, quantity, price), total, status, createdAt

## Technical Requirements
- Spring Boot 3.4.2, Java 21
- H2 in-memory database with JPA
- No authentication required (public API)
- All monetary values use BigDecimal
- Package: com.freshmart

## Non-Goals (out of scope)
- Payment processing (just create order, no actual payment)
- User accounts / authentication
- Real-time inventory tracking
- Delivery scheduling

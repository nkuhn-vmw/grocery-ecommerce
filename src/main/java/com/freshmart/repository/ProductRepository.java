package com.freshmart.repository;

import com.freshmart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    java.util.List<Product> findByCategory(com.freshmart.model.Category category);
}

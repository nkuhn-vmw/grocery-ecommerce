package com.freshmart;

import com.freshmart.model.Product;
import com.freshmart.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            productRepository.save(createProduct("Organic Bananas", BigDecimal.valueOf(1.29)));
            productRepository.save(createProduct("Whole Milk", BigDecimal.valueOf(4.99)));
            productRepository.save(createProduct("Sourdough Bread", BigDecimal.valueOf(5.49)));
            productRepository.save(createProduct("Chicken Breast", BigDecimal.valueOf(8.99)));
            productRepository.save(createProduct("Orange Juice", BigDecimal.valueOf(3.79)));
            productRepository.save(createProduct("Dark Chocolate", BigDecimal.valueOf(2.99)));
        }
    }

    private Product createProduct(String name, BigDecimal price) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        return p;
    }
}

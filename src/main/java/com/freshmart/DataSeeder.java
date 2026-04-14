package com.freshmart;

import com.freshmart.model.Category;
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
            // Create and save sample products
            productRepository.save(new Product(null, "Organic Bananas", "Fresh organic bananas", BigDecimal.valueOf(1.29), Category.PRODUCE, "image1.jpg", 50));
            productRepository.save(new Product(null, "Whole Milk", "1 gallon whole milk", BigDecimal.valueOf(4.99), Category.DAIRY, "image2.jpg", 30));
            productRepository.save(new Product(null, "Sourdough Bread", "Fresh sourdough bread", BigDecimal.valueOf(5.49), Category.BAKERY, "image3.jpg", 25));
            productRepository.save(new Product(null, "Chicken Breast", "Boneless skinless chicken breast", BigDecimal.valueOf(8.99), Category.MEAT, "image4.jpg", 20));
            productRepository.save(new Product(null, "Orange Juice", "Fresh squeezed orange juice", BigDecimal.valueOf(3.79), Category.BEVERAGES, "image5.jpg", 40));
            productRepository.save(new Product(null, "Dark Chocolate", "70% dark chocolate bar", BigDecimal.valueOf(2.99), Category.SNACKS, "image6.jpg", 60));
        }
    }
}
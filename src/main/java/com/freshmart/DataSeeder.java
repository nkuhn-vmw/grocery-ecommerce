package com.freshmart;

import com.freshmart.model.Product;
import com.freshmart.model.Category;
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
            productRepository.save(createProduct("Organic Bananas", BigDecimal.valueOf(1.29), Category.PRODUCE, "Delicious organic bananas.", "/images/bananas.png"));
            productRepository.save(createProduct("Whole Milk", BigDecimal.valueOf(4.99), Category.DAIRY, "Creamy whole milk.", "/images/milk.png"));
            productRepository.save(createProduct("Sourdough Bread", BigDecimal.valueOf(5.49), Category.BAKERY, "Artisan sourdough bread.", "/images/bread.png"));
            productRepository.save(createProduct("Chicken Breast", BigDecimal.valueOf(8.99), Category.MEAT, "Boneless chicken breast.", "/images/chicken.png"));
            productRepository.save(createProduct("Orange Juice", BigDecimal.valueOf(3.79), Category.BEVERAGES, "Fresh orange juice.", "/images/oj.png"));
            productRepository.save(createProduct("Dark Chocolate", BigDecimal.valueOf(2.99), Category.SNACKS, "Rich dark chocolate.", "/images/chocolate.png"));
        }
    }

    private Product createProduct(String name, BigDecimal price, Category category, String description, String imageUrl) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setCategory(category);
        p.setDescription(description);
        p.setStockQuantity(50);
        p.setImageUrl(imageUrl);
        return p;
    }
}

package com.hnh;

import com.hnh.repository.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DbCheck {
    @Autowired
    ProductRepository productRepository;

    @Test
    void checkCount() {
        System.out.println("TOTAL PRODUCTS: " + productRepository.count());
    }
}

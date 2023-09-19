package com.beetech.springsecurity.domain.seedData;

import com.beetech.springsecurity.SpringSecurityApplication;
import com.beetech.springsecurity.domain.entity.Product;
import com.beetech.springsecurity.domain.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SeedDataProduct {
    private static final Logger log = LoggerFactory.getLogger(SpringSecurityApplication.class);

    @Bean
    public CommandLineRunner seedProducts(ProductRepository productRepository) {
        return (args) -> {

            productRepository.save(new Product(1, "MGF543GHGF", "Product A", "detail_info 1", 20_000L, 0));
            productRepository.save(new Product(2, "GJJ457KJGF", "Product B", "detail_info 2", 10_000L, 0));
            productRepository.save(new Product(3, "HGV789TFDE", "Product C", "detail_info 3", 30_000L, 0));
            productRepository.save(new Product(4, "GJJ459LHGF", "Product D", "detail_info 4", 40_000L, 0));


            log.info("Product found with findAll():");
            log.info("-------------------------------");
            for (Product product : productRepository.findAll()) {
                log.info(product.toString());
            }
            log.info("");


        };
    }
}

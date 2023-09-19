package com.beetech.springsecurity.domain.seedData;

import com.beetech.springsecurity.SpringSecurityApplication;
import com.beetech.springsecurity.domain.entity.Category;
import com.beetech.springsecurity.domain.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SeedDataCategories {
    private static final Logger log = LoggerFactory.getLogger(SpringSecurityApplication.class);

    @Bean
    public CommandLineRunner seedCategories(CategoryRepository categoryRepository) {
        return (args) -> {

//            categoryRepository.save(new Category(1, "Category 1"));
//            categoryRepository.save(new Category(2, "Category 2"));
//            categoryRepository.save(new Category(3, "Category 3"));
//            categoryRepository.save(new Category(4, "Category 4"));
//            categoryRepository.save(new Category(5, "Category 5"));


            log.info("Categories found with findAll():");
            log.info("-------------------------------");
            for (Category category : categoryRepository.findAll()) {
                log.info(category.toString());
            }
            log.info("");


        };
    }
}

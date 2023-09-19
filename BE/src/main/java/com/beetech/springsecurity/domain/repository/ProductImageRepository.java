package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.CategoryImage;
import com.beetech.springsecurity.domain.entity.Product;
import com.beetech.springsecurity.domain.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

    @Query("SELECT pi FROM ProductImage pi JOIN FETCH pi.product p WHERE p.id = :productId")
    List<ProductImage> findAllByProductId(@Param("productId")int productId);
    List<ProductImage>  findAllByProduct(Product product);
}

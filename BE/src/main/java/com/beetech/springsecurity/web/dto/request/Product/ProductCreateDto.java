package com.beetech.springsecurity.web.dto.request.Product;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ProductCreateDto {
    private String sku;
    private String name;
    private String detailInfo;
    private long price;
    private int categoryId;

    @NotNull
    private MultipartFile thumbnailImage;
    @NotNull
    private List<MultipartFile> detailImage;


    // Constructors, getters, setters, and toString
}
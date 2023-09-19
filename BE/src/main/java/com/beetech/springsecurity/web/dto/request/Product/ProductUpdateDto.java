package com.beetech.springsecurity.web.dto.request.Product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductUpdateDto {
    private String sku;
    private String name;
    private String detailInfo;
    private int categoryId;
    private long price;
    @NotNull
    private MultipartFile thumbnailImage;
    @NotNull
    private List<MultipartFile> detailImage;
}

package com.beetech.springsecurity.web.dto.request.Category;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDto {
    private int id;
    @NotNull
    private String name;
    @NotNull
    private MultipartFile image;
}

package com.beetech.springsecurity.domain.service;

import com.beetech.springsecurity.domain.entity.Category;
import com.beetech.springsecurity.domain.entity.CategoryImage;
import com.beetech.springsecurity.domain.entity.Image;
import com.beetech.springsecurity.domain.repository.CategoryImageRepository;
import com.beetech.springsecurity.domain.repository.CategoryRepository;
import com.beetech.springsecurity.domain.repository.ImageRepository;
import com.beetech.springsecurity.web.dto.request.Category.CategoryCreateDto;
import com.beetech.springsecurity.web.dto.response.Category.CategoryDto;
import com.beetech.springsecurity.web.dto.request.Category.CategoryUpdateDto;
import com.beetech.springsecurity.web.dto.response.Category.ImageOfCategoryDto;
import com.beetech.springsecurity.web.dto.response.User.ResponseDto;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final CategoryImageRepository categoryImageRepository;

    public List<CategoryDto> getCategories() {
        List<Category> categories = Lists.newArrayList(categoryRepository.findAll());
        List<CategoryDto> categoryDtos = new ArrayList<>();

        //transfer from category to category DTO including image
        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());


            ImageOfCategoryDto imageOfCategoryDto = modelMapper
                    .map(category.getCategoryImages(),ImageOfCategoryDto.class);

            String path = category.getCategoryImages().getImage().getPath();
            String name = category.getCategoryImages().getImage().getName();


            categoryDto.setImageOfCategoryDto(imageOfCategoryDto);
            categoryDto.getImageOfCategoryDto().getImage().setName(name);
            categoryDto.getImageOfCategoryDto().getImage().setPath(path);

            categoryDtos.add(categoryDto);
        }
        return categoryDtos;
    }

    public CategoryDto getCategoryById(int categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        return modelMapper.map(categoryOptional.get(), CategoryDto.class);
    }

    @Transactional
    public ResponseDto createCategory(CategoryCreateDto categoryCreateDto) {
        Category category = modelMapper.map(categoryCreateDto, Category.class);
        var userResponse = new ResponseDto();
        Image image = new Image();
        try {
            Category createdCategory = categoryRepository.save(category);
            image.setName(categoryCreateDto.getImage().getOriginalFilename());
            image.setPath(importImage(categoryCreateDto.getImage(),categoryCreateDto.getName()));

            Image createdImage = imageRepository.save(image);

            categoryImageRepository.save(new CategoryImage(createdCategory,createdImage));

            userResponse.setMessage("Category created ...");
            userResponse.setStatus(200);
        } catch (RuntimeException e) {
            userResponse.setMessage("Some thing went wrong ...");
            userResponse.setStatus(401);
        }
        return userResponse;
    }
    @Transactional
    public ResponseDto updateCategory(int categoryId, CategoryUpdateDto categoryUpdateDto) {
        Image image = new Image();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));


        deleteImage(category.getName());

        category.setName(categoryUpdateDto.getName());
        Category updatedCategory = categoryRepository.save(category);


        image.setName(categoryUpdateDto.getImage().getOriginalFilename());
        image.setPath(importImage(categoryUpdateDto.getImage(),categoryUpdateDto.getName()));

        Image updatedImage = imageRepository.save(image);

        CategoryImage categoryImage = categoryImageRepository.findByCategoryId(updatedCategory.getId());
        categoryImageRepository.save(new CategoryImage(categoryImage.getId(),updatedCategory,updatedImage));

        var response = new ResponseDto();
        try {
            categoryRepository.save(category);
            response.setMessage("Category updated ...");
            response.setStatus(200);
        } catch (RuntimeException e) {
            response.setMessage("Some thing went wrong ...");
            response.setStatus(401);
        }
        return response;
    }

    public ResponseDto deleteCategory(int categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        var response = new ResponseDto();
        try {
            categoryRepository.delete(category);
            response.setMessage("Category deleted ...");
            response.setStatus(200);
        } catch (RuntimeException e) {
            response.setMessage("Some thing went wrong ...");
            response.setStatus(401);
        }
        return response;
    }

    public String importImage(MultipartFile file, String categoryName) {
        try {
            String fileName = file.getOriginalFilename();

            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!fileExtension.equalsIgnoreCase("jpg")) {
                throw new RuntimeException("File not accepted. Only JPG files are allowed.");
            }

            String uploadDirectory = "src/main/resources/images/category";
            Path uploadDirectoryPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadDirectoryPath)) {
                Files.createDirectories(uploadDirectoryPath);
            }

            String newFileName = categoryName + "." + fileExtension;
            Files.copy(file.getInputStream(), uploadDirectoryPath.resolve(newFileName));

            String fileUrl = "images/category/" + newFileName;
            return fileUrl;
        } catch (IOException e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }
    public boolean deleteImage(String fileName) {
        String uploadDirectory = "src/main/resources/images/category";
        Path uploadDirectoryPath = Paths.get(uploadDirectory);
        Path filePath = uploadDirectoryPath.resolve(fileName+".jpg");

        if (Files.exists(filePath)) {
            // Check if the file is in use or locked
            try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.WRITE)) {
                // File is not in use, proceed with deletion
                Files.delete(filePath);
                return true;
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file: File is in use or locked by another process.");
            }
        } else {
            throw new RuntimeException("File not found.");
        }
    }
}

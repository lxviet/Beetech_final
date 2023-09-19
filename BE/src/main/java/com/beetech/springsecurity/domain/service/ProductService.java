package com.beetech.springsecurity.domain.service;

import com.beetech.springsecurity.domain.entity.*;
import com.beetech.springsecurity.domain.repository.CategoryRepository;
import com.beetech.springsecurity.domain.repository.ImageRepository;
import com.beetech.springsecurity.domain.repository.ProductImageRepository;
import com.beetech.springsecurity.domain.repository.ProductRepository;
import com.beetech.springsecurity.web.dto.request.Product.ProductCreateDto;
import com.beetech.springsecurity.web.dto.response.Product.ProductDto;
import com.beetech.springsecurity.web.dto.request.Product.ProductUpdateDto;
import com.beetech.springsecurity.web.dto.response.User.ResponseDto;
import com.google.common.collect.Lists;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    public List<ProductDto> getProducts() {
        List<Product> products = Lists.newArrayList(productRepository.findAll());
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());

    }


    public ProductDto getById(int productId) {
        Product products = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        return modelMapper.map(products, ProductDto.class);
    }
    @Transactional
    public ResponseDto createProduct(ProductCreateDto productCreateDto) {
        var response = new ResponseDto();
        List<MultipartFile> files = productCreateDto.getDetailImage();
        List<Image> images = new ArrayList<>();
        List<String> imageName = new ArrayList<>();
        Optional<Category> categoryOptional = categoryRepository.findById(productCreateDto.getCategoryId());
        int i = 0;
        try {
            Product product = new Product(); // Create a new instance of the Product

            product.setName(productCreateDto.getName());
            product.setDelete_flag(0);
            product.setSku(RandomString.make(10));
            product.setCategory(categoryOptional.get());
            product.setDetailInfo(productCreateDto.getDetailInfo());
            product.setPrice(productCreateDto.getPrice());
            Product createdProduct = productRepository.save(product);
            files.add(productCreateDto.getThumbnailImage());

            List<String> fileUrls = importImages(files, productCreateDto.getName());
            files.forEach(f -> imageName.add(f.getName()));

            for (String fileUrl : fileUrls) {
                Image image = new Image();
                image.setPath(fileUrl);
                image.setName(imageName.get(i));
                images.add(image);
                i++;
            }
            images.get(images.size() - 1).setThumb_flag(1);
            List<Image> createdImages = imageRepository.saveAll(images);
            for (Image createdImage : createdImages) {
                productImageRepository.save(new ProductImage(createdProduct, createdImage));
            }

            response.setMessage("Product created...");
            response.setStatus(200);

        } catch (RuntimeException e) {
            response.setMessage("Something went wrong...");
            response.setStatus(401);
        }

        return response;
    }

    @Transactional
    public ResponseDto updateProduct(ProductUpdateDto productUpdateDto) {
        try {
            Optional<Product> optionalProduct = productRepository.findBySku(productUpdateDto.getSku());

            if (optionalProduct.isEmpty()) {
                ResponseDto response = new ResponseDto();
                response.setMessage("Product not found.");
                response.setStatus(404);
                return response;
            }

            Product existingProduct = optionalProduct.get();
            existingProduct.setName(productUpdateDto.getName());
            existingProduct.setPrice(productUpdateDto.getPrice());
            // Save the updated product
            Product updatedProduct = productRepository.save(existingProduct);

            // Delete the existing product images
            List<ProductImage> productImages = productImageRepository.findAllByProduct(updatedProduct);

            for (ProductImage productImage : productImages) {
                Image image = productImage.getImage();
                productImageRepository.delete(productImage);
                imageRepository.delete(image);
                // Delete the image file
                String productImagePath = image.getPath();
                String fileName = productImagePath.replaceAll(".*/", "");
                deleteImageFile(fileName);
            }

            // Save the updated images
            List<MultipartFile> files = productUpdateDto.getDetailImage();
            files.add(productUpdateDto.getThumbnailImage());
            List<Image> images = new ArrayList<>();
            List<String> imageNames = new ArrayList<>();
            int i = 0;

            List<String> fileUrls = importImages(files, productUpdateDto.getName());

            for (String fileUrl : fileUrls) {
                Image image = new Image();
                image.setPath(fileUrl);
                image.setName(fileUrl.substring(fileUrl.lastIndexOf('/') + 1));
                images.add(image);
                imageNames.add(image.getName());
                i++;
            }

            images.get(images.size() - 1).setThumb_flag(1);

            List<Image> createdImages = imageRepository.saveAll(images);

            for (Image createdImage : createdImages) {
                productImageRepository.save(new ProductImage(updatedProduct, createdImage));
            }

            ResponseDto response = new ResponseDto();
            response.setMessage("Product updated ...");
            response.setStatus(200);
            return response;
        } catch (RuntimeException e) {
            ResponseDto response = new ResponseDto();
            response.setMessage("Something went wrong.");
            response.setStatus(401);
            return response;
        }
    }



    private void deleteImageFile(String fileName) {
        String uploadDirectory = "src/main/resources/images/product";
        Path filePath = Paths.get(uploadDirectory).resolve(fileName);

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Failed to delete image file: " + fileName);
        }
    }


//    public UserManagerResponse deleteProduct(@PathVariable int productId) {
//        var userResponse = new UserManagerResponse();
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        try {
//            productRepository.delete(product);
//            userResponse.setMessage("Product delete ...");
//            userResponse.setExpireDate(new Date());
//            userResponse.setIsSuccess(true);
//        } catch (RuntimeException e) {
//            userResponse.setMessage("Some thing went wrong ...");
//            userResponse.setExpireDate(new Date());
//            userResponse.setIsSuccess(true);
//        }
//        return userResponse;
//    }
    public List<String> importImages(List<MultipartFile> files, String productName) {
        List<String> fileUrls = new ArrayList<>();

        try {
            String uploadDirectory = "src/main/resources/images/product";
            Path uploadDirectoryPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadDirectoryPath)) {
                Files.createDirectories(uploadDirectoryPath);
            }
            int i = 1;
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

                if (!fileExtension.equalsIgnoreCase("jpg")) {
                    throw new RuntimeException("File not accepted. Only JPG files are allowed.");
                }

                String newFileName = productName+"_Detail"+ i + "." + fileExtension;
                Files.copy(file.getInputStream(), uploadDirectoryPath.resolve(newFileName));

                String fileUrl = "images/product/" + newFileName;
                fileUrls.add(fileUrl);
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload files: " + e.getMessage());
        }

        return fileUrls;
    }
    public boolean deleteImages(List<String> fileNames) {
        String uploadDirectory = "src/main/resources/images/category";
        Path uploadDirectoryPath = Paths.get(uploadDirectory);
        boolean allDeleted = true;

        for (String fileName : fileNames) {
            Path filePath = uploadDirectoryPath.resolve(fileName + ".jpg");

            if (Files.exists(filePath)) {
                try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.WRITE)) {
                    // File is not in use, proceed with deletion
                    Files.delete(filePath);
                } catch (IOException e) {
                    // File is in use or locked
                    allDeleted = false;
                    System.err.println("Failed to delete file: " + fileName + ".jpg");
                }
            } else {
                // File not found
                allDeleted = false;
                System.err.println("File not found: " + fileName + ".jpg");
            }
        }

        return allDeleted;
    }
}

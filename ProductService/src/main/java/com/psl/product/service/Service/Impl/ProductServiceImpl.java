package com.psl.product.service.Service.Impl;


import com.psl.product.service.Entity.Product;
import com.psl.product.service.Exceptions.ResourceNotFoundException;
import com.psl.product.service.Repository.ProductRepo;
import com.psl.product.service.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepo productRepo;

    @Value("${file.upload-dir}")
    private String uploadDir; // Using the value from application.properties

    @Override
    public Product addProduct(Product product) throws IOException {
        return productRepo.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public Product getProductByProductId(int productId) {
        return productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("The Product with given id not exists"));
    }

    @Override
    public void deleteProduct(int productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("The Product with given id not exists"));
        productRepo.delete(product);
    }

    @Override
    public Product updateProduct(Product product, int productId) {
        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("The Product with the given ID does not exist"));
        existingProduct.setProductCategory(product.getProductCategory());
        existingProduct.setProductName(product.getProductName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setImageUrl(product.getImageUrl());
        return productRepo.save(existingProduct);
    }

    @Override
    public String saveImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Image file cannot be empty.");
        }
        // Generate the file path based on the upload directory
        Path imagePath = Paths.get(uploadDir + image.getOriginalFilename());
        // Ensure the directory exists
        Files.createDirectories(imagePath.getParent());
        // Write the file to the directory
        Files.write(imagePath, image.getBytes());
        // Return the image URL for the saved image
        return imagePath.toString();
    }
}




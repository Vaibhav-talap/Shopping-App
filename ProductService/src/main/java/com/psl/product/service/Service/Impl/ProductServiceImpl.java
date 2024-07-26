package com.psl.product.service.Service.Impl;


import com.psl.product.service.Entity.Product;
import com.psl.product.service.Exceptions.ResourceNotFoundException;
import com.psl.product.service.Repository.ProductRepo;
import com.psl.product.service.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepo productRepo;

    @Override
    public Product addProduct(Product product) {
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
        Product buProduct = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("The Product with given id not exists"));
        buProduct.setProductCategory(product.getProductCategory());
        buProduct.setProductName(product.getProductName());
        return productRepo.save(buProduct);
    }
}


package com.psl.product.service.Service;

import com.psl.product.service.Entity.Product;

import java.util.List;

public interface ProductService {

    Product addProduct(Product product);

    List<Product> getAllProducts();

    Product getProductByProductId(int productId);

    void deleteProduct(int productId);


    Product updateProduct(Product product, int productId);
}

package com.psl.product.service.Service;

import com.psl.product.service.Entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    Product addProduct(Product product) throws IOException;

    List<Product> getAllProducts();

    Product getProductByProductId(int productId);

    void deleteProduct(int productId);


    Product updateProduct(Product product, int productId);

    String saveImage(MultipartFile image) throws IOException;
}

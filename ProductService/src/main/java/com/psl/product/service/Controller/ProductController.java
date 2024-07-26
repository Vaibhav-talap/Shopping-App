package com.psl.product.service.Controller;

import com.psl.product.service.Entity.Product;
import com.psl.product.service.Payload.ApiResponse;
import com.psl.product.service.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Products")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        return new ResponseEntity<>(productService.addProduct(product), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductbyId(@PathVariable int productId) {
        return new ResponseEntity<>(productService.getProductByProductId(productId), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deletebyProductbyId(@PathVariable int productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(new ApiResponse("Product Deleted SuccessFully.", true, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updatebyProductbyId(@Valid @RequestBody Product product, @PathVariable int productId) {
        return new ResponseEntity<>(productService.updateProduct(product, productId), HttpStatus.ACCEPTED);
    }
}

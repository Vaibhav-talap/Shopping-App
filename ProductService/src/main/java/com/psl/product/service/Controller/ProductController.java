package com.psl.product.service.Controller;

import com.psl.product.service.Entity.Product;
import com.psl.product.service.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.psl.product.service.Payload.ApiRespons;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/Products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Operation(summary = "Create a new product", description = "Adds a product with an image, category, and price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestParam("productName") String productName,
                                                 @RequestParam("productCategory") String productCategory,
                                                 @RequestParam("price") double price,
                                                 @RequestParam("image") MultipartFile image) throws IOException {
        // Save the image and get the file path
        String imageUrl = productService.saveImage(image);
        // Create a new product object
        Product product = Product.builder()
                .productName(productName)
                .productCategory(productCategory)
                .price(price)
                .imageUrl(imageUrl)
                .build();
        // Save the product to the database
        return new ResponseEntity<>(productService.addProduct(product), HttpStatus.CREATED);
    }



    @Operation(summary = "Get all products", description = "Fetches a list of all products")
    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @Operation(summary = "Get product by ID", description = "Fetches product details by its ID")
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable int productId) {
        return new ResponseEntity<>(productService.getProductByProductId(productId), HttpStatus.OK);
    }

    @Operation(summary = "Delete product by ID", description = "Deletes a product by its ID")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiRespons> deleteByProductById(@PathVariable int productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(new ApiRespons("Product Deleted SuccessFully.", true, HttpStatus.OK), HttpStatus.OK);
    }

    @Operation(summary = "Update product by ID", description = "Updates product details by its ID")
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateByProductById(@Valid @RequestBody Product product, @PathVariable int productId) {
        return new ResponseEntity<>(productService.updateProduct(product, productId), HttpStatus.ACCEPTED);
    }

}

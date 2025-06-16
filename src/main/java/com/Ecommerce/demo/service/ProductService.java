package com.Ecommerce.demo.service;

import com.Ecommerce.demo.exceptions.ProductException;
import com.Ecommerce.demo.model.Product;
import com.Ecommerce.demo.model.Seller;
import com.Ecommerce.demo.requests.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest req, Seller seller);
    public void deleteProduct(Long productId) throws ProductException;
    public Product updateProduct(Long productId,Product product) throws ProductException;
    Product findProductById(Long productId) throws ProductException;
    List<Product> searchProducts(String queryS);
    public Page<Product> getAllProducts(
        String category,
        String brand,
        String colors,
        String sizes,
        String minPrice,
        String maxPrice,
        Integer minDiscount,
        String sort,
        String stock,
        Integer pageNumber
    );
    List<Product> getProductBySellerId(Long sellerId);

}

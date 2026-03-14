package com.Ecommerce.demo.service.impl;

import com.Ecommerce.demo.exceptions.ProductException;
import com.Ecommerce.demo.model.Category;
import com.Ecommerce.demo.model.Product;
import com.Ecommerce.demo.model.Seller;
import com.Ecommerce.demo.repository.CategoryRepository;
import com.Ecommerce.demo.repository.ProductRepository;
import com.Ecommerce.demo.requests.CreateProductRequest;
import com.Ecommerce.demo.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    //women leather bags
    //category 1 -> women fashion
    //category 2 -> women accessories
    //category 3 -> handbags
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {
        Category category1 = categoryRepository.findByCategoryId(req.getCategory1());
        if(category1 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory1());
            category.setLevel(1);
            category1 = categoryRepository.save(category);
        }

        Category category2 = categoryRepository.findByCategoryId(req.getCategory2());
        if(category2 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory2());
            category.setLevel(2);
            category.setParentCategory(category1);
            category2 = categoryRepository.save(category);
        }

        Category category3 = categoryRepository.findByCategoryId(req.getCategory3());
        if (category3 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory3());
            category.setLevel(3);
            category.setParentCategory(category2);
            category3 = categoryRepository.save(category);
        }
        int discount = calculateDiscountPercent(req.getMrpPrice(),req.getSellingPrice());
        Product product = new Product();
        product.setCreatedAt(LocalDateTime.now());
        product.setColor(req.getColor());
        product.setCategory(category3);
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setSellingprice(req.getSellingPrice());
        product.setTitle(req.getTitle());
        product.setSeller(seller);
        product.setSizes(req.getSizes());
        product.setDescription(req.getDescription());
        product.setDiscountPercent(discount);

        return productRepository.save(product);
    }

    public int calculateDiscountPercent(int mrpPrice,int sellingPrice){
        if (mrpPrice <= 0){
            throw new IllegalArgumentException("Actual price must be greater than 0");
        }
        double discount = mrpPrice-sellingPrice;
        double discountPercentage = (discount/mrpPrice)*100;
        return (int) discountPercentage;
    }
    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        findProductById(productId);
        product.setId(productId);

        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(()->new ProductException("product not found with this id"));
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProducts(query);
    }


    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, String minPrice, String maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        Specification<Product> specs = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (category != null && !category.isEmpty()){
                Join<Product,Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"),category));
            }
            if (colors != null && !colors.isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("color"),colors));
            }
            if(sizes!= null && !sizes.isEmpty()){
                predicates.add(criteriaBuilder.like(root.get("sizes"), "%" + sizes + "%"));
            }
            if(minPrice != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingprice"), Integer.parseInt(minPrice)));
            }
            if (maxPrice != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingprice"), Integer.parseInt(maxPrice)));
            }
            if (minDiscount != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }
            if (stock != null){
                predicates.add(criteriaBuilder.greaterThan(root.get("quantity"), 0));
            }
            return criteriaBuilder.and(predicates.toArray((new Predicate[0])));
        };
        Sort sortObj = createSortObject(sort);
        Pageable pageable = PageRequest.of(pageNumber!=null?pageNumber:0,10,sortObj);
        return productRepository.findAll(specs,pageable);
    }

    public Sort createSortObject(String sort){
        if (sort == null){
            return Sort.by(Sort.Direction.DESC,"createdAt");
        }
        switch (sort){
            case "price_low":
                return Sort.by(Sort.Direction.ASC,"sellingprice");
            case "price_high":
                return Sort.by(Sort.Direction.DESC,"sellingprice");
            case "newest":
                return Sort.by(Sort.Direction.DESC,"createdAt");
            case "popular":
                return Sort.by(Sort.Direction.DESC,"numRatings");
            case "rating":
                return Sort.by(Sort.Direction.DESC,"numRatings");
            default:
                return Sort.by(Sort.Direction.DESC , "numRatings","createdAt","sellingprice");
        }
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findProductBySellerId(sellerId);
    }
}

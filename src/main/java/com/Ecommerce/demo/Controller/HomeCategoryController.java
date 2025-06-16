package com.Ecommerce.demo.Controller;

import com.Ecommerce.demo.model.Home;
import com.Ecommerce.demo.model.HomeCategory;
import com.Ecommerce.demo.service.HomeCategoryService;
import com.Ecommerce.demo.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeCategoryController {
    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategories(@RequestBody List<HomeCategory> homeCategories){
        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
        Home home = homeService.createHomePageData(categories);
        return new ResponseEntity<>(home, HttpStatus.OK);
    }

    @GetMapping("/admin/home-category")
    public ResponseEntity<List<HomeCategory>> getHomeCategory(){
        List<HomeCategory> categories = homeCategoryService.getAllHomeCategories();
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @PatchMapping("/home-category/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(@PathVariable Long id , @RequestBody HomeCategory homeCategory) throws Exception {
        HomeCategory updatedhomeCategory = homeCategoryService.updateHomeCategories(homeCategory,id);
        return new ResponseEntity<>(updatedhomeCategory,HttpStatus.OK);
    }
}

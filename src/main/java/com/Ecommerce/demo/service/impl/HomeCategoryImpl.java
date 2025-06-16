package com.Ecommerce.demo.service.impl;

import com.Ecommerce.demo.model.HomeCategory;
import com.Ecommerce.demo.repository.HomeCategoryRepository;
import com.Ecommerce.demo.service.HomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class HomeCategoryImpl implements HomeCategoryService {

    private final HomeCategoryRepository homeCategoryRepository;
    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return homeCategoryRepository.save(homeCategory);
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> categories) {
        if (homeCategoryRepository.findAll().isEmpty()){
            homeCategoryRepository.saveAll(categories);
        }
        return homeCategoryRepository.findAll();
    }

    @Override
    public HomeCategory updateHomeCategories(HomeCategory homeCategory,Long id) throws Exception {
        HomeCategory existingHomeCategory = homeCategoryRepository.findById(id).orElseThrow(()->new Exception("no category exists by this id"));
        if (homeCategory.getImage() != null){
            existingHomeCategory.setImage(homeCategory.getImage());
        }
        if (homeCategory.getCategoryId() != null){
            existingHomeCategory.setCategoryId(homeCategory.getCategoryId());
        }
        return homeCategoryRepository.save(existingHomeCategory);
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {
        return homeCategoryRepository.findAll();
    }
}

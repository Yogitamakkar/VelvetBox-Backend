package com.Ecommerce.demo.service;

import com.Ecommerce.demo.model.Home;
import com.Ecommerce.demo.model.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);
}

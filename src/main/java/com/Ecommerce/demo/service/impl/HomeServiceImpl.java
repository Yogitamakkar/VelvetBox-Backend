package com.Ecommerce.demo.service.impl;

import com.Ecommerce.demo.domain.HomeCategorySection;
import com.Ecommerce.demo.model.Deal;
import com.Ecommerce.demo.model.Home;
import com.Ecommerce.demo.model.HomeCategory;
import com.Ecommerce.demo.repository.DealRepository;
import com.Ecommerce.demo.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final DealRepository dealRepository;

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
        List<HomeCategory> gridCategories = allCategories.stream().filter(
                homeCategory -> homeCategory.getSection() == HomeCategorySection.GRID
        ).collect(Collectors.toList());

        List<HomeCategory> shopByCategories = allCategories.stream().filter(
                homeCategory -> homeCategory.getSection() == HomeCategorySection.SHOP_BY_CATEGORIES
        ).collect(Collectors.toList());

        List<HomeCategory> electricCategories = allCategories.stream().filter(
                homeCategory -> homeCategory.getSection() == HomeCategorySection.ELECTRIC_CATEGORIES
        ).collect(Collectors.toList());

        List<HomeCategory> dealCategories = allCategories.stream().filter(
                homeCategory -> homeCategory.getSection() == HomeCategorySection.DEALS
        ).toList();

        List<Deal> createdDeals = new ArrayList<>();

        if (dealRepository.findAll().isEmpty()){
            List<Deal> deals = allCategories.stream().filter(
                    homeCategory -> homeCategory.getSection() == HomeCategorySection.DEALS
            ).map(homeCategory -> new Deal(null,10,homeCategory))
                    .collect(Collectors.toList());

            createdDeals = dealRepository.saveAll(deals);
        }else createdDeals = dealRepository.findAll();

        Home home = new Home();
        home.setGrid(gridCategories);
        home.setDeals(createdDeals);
        home.setElectricCategories(electricCategories);
        home.setShopByCategories(shopByCategories);
        home.setDealCategories(dealCategories);

        return home;
    }
}

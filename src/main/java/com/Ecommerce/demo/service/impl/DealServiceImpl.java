package com.Ecommerce.demo.service.impl;

import com.Ecommerce.demo.model.Deal;
import com.Ecommerce.demo.model.HomeCategory;
import com.Ecommerce.demo.repository.DealRepository;
import com.Ecommerce.demo.repository.HomeCategoryRepository;
import com.Ecommerce.demo.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public List<Deal> getDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
        deal.setCategory(category);
        deal.setDiscount(deal.getDiscount());
        return dealRepository.save(deal);
    }

    @Override
    public Deal updateDeal(Deal deal,Long id) throws Exception {
        Deal existingDeal = dealRepository.findById(id).orElse(null);
        if (existingDeal != null){
            if (deal.getDiscount() != null){
                existingDeal.setDiscount(deal.getDiscount());
            }
            if (deal.getCategory() != null){
                existingDeal.setCategory(deal.getCategory());
            }
        }
        throw new Exception("deal not found");
    }

    @Override
    public void deleteDeal(Long id) throws Exception {
        Deal deal = dealRepository.findById(id).orElseThrow(()->new Exception("no deal found with this id"));
        dealRepository.delete(deal);
    }
}

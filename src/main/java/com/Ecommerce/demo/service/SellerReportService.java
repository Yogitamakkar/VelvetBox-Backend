package com.Ecommerce.demo.service;

import com.Ecommerce.demo.model.Seller;
import com.Ecommerce.demo.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
}

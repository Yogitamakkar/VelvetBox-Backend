package com.Ecommerce.demo.service.impl;

import com.Ecommerce.demo.model.Order;
import com.Ecommerce.demo.model.Seller;
import com.Ecommerce.demo.model.Transaction;
import com.Ecommerce.demo.repository.SellerReportRepository;
import com.Ecommerce.demo.repository.SellerRepository;
import com.Ecommerce.demo.repository.TransactionRepository;
import com.Ecommerce.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    @Override
    public Transaction createTransaction(Order order) {
        Seller seller = sellerRepository.findById(order.getSellerId()).get();

        Transaction transaction = new Transaction();
        transaction.setSeller(seller);
        transaction.setCustomer(order.getUser());
        transaction.setOrder(order);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionBySellerId(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}

package com.Ecommerce.demo.service;

import com.Ecommerce.demo.model.Order;
import com.Ecommerce.demo.model.Seller;
import com.Ecommerce.demo.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);
    List<Transaction> getTransactionBySellerId(Seller seller);
    List<Transaction> getAllTransactions();
 }

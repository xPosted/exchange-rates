package com.example.exchange.db.repo;

import com.example.exchange.db.entity.SymbolExchangeRatesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExchangeRatesRepo extends MongoRepository<SymbolExchangeRatesEntity, String> {
    // You can define custom query methods here if needed
}


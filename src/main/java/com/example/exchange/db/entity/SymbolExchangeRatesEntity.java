package com.example.exchange.db.entity;

import com.example.exchange.integration.exchangerate.dto.SymbolEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@Document(collection = "exchange_sample")
public class SymbolExchangeRatesEntity {
    @Id
    private String id;
    private long timestamp;
    private SymbolEnum source;
    private Map<SymbolEnum, Double> quotes;

    public static String generateId(SymbolEnum symbol) {
        return String.format("%s-%s", "ExchangeRate-", symbol.name());
    }

}


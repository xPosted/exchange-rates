package com.example.exchange.web.dto;

import com.example.exchange.db.entity.SymbolExchangeRatesEntity;
import com.example.exchange.integration.exchangerate.dto.SymbolEnum;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ExchangeRateInfo {

    private long timestamp;
    private SymbolEnum source;
    private Map<SymbolEnum, Double> quotes;

    public static ExchangeRateInfo from(SymbolExchangeRatesEntity entity) {
        return ExchangeRateInfo.builder()
                .timestamp(entity.getTimestamp())
                .source(entity.getSource())
                .quotes(entity.getQuotes())
                .build();
    }
}

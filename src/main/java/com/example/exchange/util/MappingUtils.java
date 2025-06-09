package com.example.exchange.util;

import com.example.exchange.db.entity.SymbolExchangeRatesEntity;
import com.example.exchange.integration.exchangerate.dto.ExchangeRateResponseDto;
import com.example.exchange.integration.exchangerate.dto.SymbolEnum;

import java.util.Map;
import java.util.stream.Collectors;

public class MappingUtils {

    public static SymbolExchangeRatesEntity map(ExchangeRateResponseDto exchangeRateResponseDto) {
        var source = SymbolEnum.valueOf(exchangeRateResponseDto.getSource());
        var quotesEntity = exchangeRateResponseDto.getQuotes().entrySet().stream()
                        .collect(Collectors.toMap(entry -> map(entry.getKey(), source) , Map.Entry::getValue));
        return SymbolExchangeRatesEntity.builder()
                .id(SymbolExchangeRatesEntity.generateId(source))
                .source(source)
                .timestamp(System.currentTimeMillis())
                .quotes(quotesEntity)
                .build();
    }

    private static SymbolEnum map(String symbol, SymbolEnum base) {
        return SymbolEnum.valueOf(symbol.replace(base.name(), ""));
    }

}

package com.example.exchange.service;

import com.example.exchange.db.entity.SymbolExchangeRatesEntity;
import com.example.exchange.integration.exchangerate.dto.SymbolEnum;
import com.example.exchange.web.dto.ExchangeRateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ExchangeRatesService exchangeRatesService;

    public ExchangeRateInfo getExchangeRateInfo(SymbolEnum symbol) {
        SymbolExchangeRatesEntity exchangeRate = exchangeRatesService.getExchangeRate(symbol);
        return ExchangeRateInfo.builder()
                .source(exchangeRate.getSource())
                .timestamp(exchangeRate.getTimestamp())
                .quotes(exchangeRate.getQuotes())
                .build();
    }

    public ExchangeRateInfo getExchangeRateInfo(SymbolEnum symbol, SymbolEnum targetSymbol) {
        SymbolExchangeRatesEntity exchangeRate = exchangeRatesService.getExchangeRate(symbol);
        var resultingQuotes = exchangeRate.getQuotes().entrySet().stream()
                .filter(entry -> entry.getKey().equals(targetSymbol))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return ExchangeRateInfo.builder()
                .source(exchangeRate.getSource())
                .timestamp(exchangeRate.getTimestamp())
                .quotes(resultingQuotes)
                .build();
    }

}

package com.example.exchange.service;

import com.example.exchange.db.entity.SymbolExchangeRatesEntity;
import com.example.exchange.db.repo.ExchangeRatesRepo;
import com.example.exchange.integration.exchangerate.ExchangeRateClient;
import com.example.exchange.integration.exchangerate.dto.ExchangeRateResponseDto;
import com.example.exchange.integration.exchangerate.dto.SymbolEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.example.exchange.util.MappingUtils.map;

@Service
public class ExchangeRatesService {

    @Value("${credentials.exchangerate.access_key}")
    String accessKey;
    @Value("${exchange.rates.ttl.seconds}")
    Long exchangeRatesTtlSeconds;

    @Autowired
    private ExchangeRateClient exchangeRateClient;
    @Autowired
    private ExchangeRatesRepo exchangeRatesRepo;

    public ExchangeRateResponseDto getActualExchangeRate(SymbolEnum symbol) {
        return exchangeRateClient.getLiveRates(accessKey, symbol.name(), null, null);
    }

    @CachePut(value = "exchange-rates", key = "#symbol")
    public SymbolExchangeRatesEntity updateExchangeRates(SymbolEnum symbol) {
        var exchangeResponse = getActualExchangeRate(symbol);
        return exchangeRatesRepo.save(map(exchangeResponse));
    }

    @Cacheable("exchange-rates")
    public SymbolExchangeRatesEntity getExchangeRate(SymbolEnum symbol) {
        return exchangeRatesRepo.findById(SymbolExchangeRatesEntity.generateId(symbol))
                .filter(e -> !isExpired(e))
                .orElseGet(() -> updateExchangeRates(symbol));
    }

    private boolean isExpired(SymbolExchangeRatesEntity entity) {
        var res = (System.currentTimeMillis() - entity.getTimestamp()) / 1000 > exchangeRatesTtlSeconds;
        return res;
    }

}

package com.example.exchange.service;

import com.example.exchange.db.entity.SymbolExchangeRatesEntity;
import com.example.exchange.db.repo.ExchangeRatesRepo;
import com.example.exchange.integration.exchangerate.ExchangeRateClient;
import com.example.exchange.integration.exchangerate.dto.ExchangeRateResponseDto;
import com.example.exchange.integration.exchangerate.dto.SymbolEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExchangeRatesServiceTest {
    @Mock
    private ExchangeRatesRepo exchangeRatesRepo;
    @Mock
    private ExchangeRateClient exchangeRateClient;
    @InjectMocks
    private ExchangeRatesService exchangeRatesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set a default TTL to avoid NullPointerException in tests
        exchangeRatesService.exchangeRatesTtlSeconds = 60L;
    }

    @Test
    void testGetExchangeRate_CacheMiss() {
        SymbolExchangeRatesEntity entity = SymbolExchangeRatesEntity.builder()
                .source(SymbolEnum.USD)
                .timestamp(1234567890L)
                .quotes(new HashMap<>())
                .build();
        ExchangeRateResponseDto responseDto = new ExchangeRateResponseDto(true, null, null, 1234567890L, "USD", new HashMap<>());
        when(exchangeRatesRepo.findById(anyString())).thenReturn(Optional.empty());
        when(exchangeRateClient.getLiveRates(any(), eq(SymbolEnum.USD.name()), eq(null), eq(null))).thenReturn(responseDto);
        when(exchangeRatesRepo.save(any())).thenReturn(entity);
        SymbolExchangeRatesEntity result = exchangeRatesService.getExchangeRate(SymbolEnum.USD);
        assertNotNull(result);
    }

    @Test
    void testGetExchangeRate_CacheHit_NotExpired() {
        SymbolExchangeRatesEntity entity = SymbolExchangeRatesEntity.builder()
                .source(SymbolEnum.USD)
                .timestamp(System.currentTimeMillis())
                .quotes(new HashMap<>())
                .build();
        when(exchangeRatesRepo.findById(anyString())).thenReturn(Optional.of(entity));
        SymbolExchangeRatesEntity result = exchangeRatesService.getExchangeRate(SymbolEnum.USD);
        assertNotNull(result);
    }
}

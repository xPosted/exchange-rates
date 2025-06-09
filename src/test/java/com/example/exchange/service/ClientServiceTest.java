package com.example.exchange.service;

import com.example.exchange.db.entity.SymbolExchangeRatesEntity;
import com.example.exchange.integration.exchangerate.dto.SymbolEnum;
import com.example.exchange.web.dto.ExchangeRateInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {
    @Mock
    private ExchangeRatesService exchangeRatesService;
    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetExchangeRateInfo() {
        Map<SymbolEnum, Double> quotes = new HashMap<>();
        quotes.put(SymbolEnum.USD, 1.0);
        SymbolExchangeRatesEntity entity = SymbolExchangeRatesEntity.builder()
                .source(SymbolEnum.USD)
                .timestamp(123456789L)
                .quotes(quotes)
                .build();
        when(exchangeRatesService.getExchangeRate(SymbolEnum.USD)).thenReturn(entity);

        ExchangeRateInfo info = clientService.getExchangeRateInfo(SymbolEnum.USD);
        assertNotNull(info);
        assertEquals(SymbolEnum.USD, info.getSource());
        assertEquals(123456789L, info.getTimestamp());
        assertEquals(1.0, info.getQuotes().get(SymbolEnum.USD));
    }

    @Test
    void testGetExchangeRateInfoWithTargetSymbol() {
        Map<SymbolEnum, Double> quotes = new HashMap<>();
        quotes.put(SymbolEnum.USD, 1.0);
        quotes.put(SymbolEnum.EUR, 0.9);
        SymbolExchangeRatesEntity entity = SymbolExchangeRatesEntity.builder()
                .source(SymbolEnum.USD)
                .timestamp(123456789L)
                .quotes(quotes)
                .build();

        when(exchangeRatesService.getExchangeRate(SymbolEnum.USD)).thenReturn(entity);

        ExchangeRateInfo info = clientService.getExchangeRateInfo(SymbolEnum.USD, SymbolEnum.EUR);
        assertNotNull(info);
        assertEquals(SymbolEnum.USD, info.getSource());
        assertEquals(123456789L, info.getTimestamp());
        assertTrue(info.getQuotes().containsKey(SymbolEnum.EUR));
        assertEquals(0.9, info.getQuotes().get(SymbolEnum.EUR));
        assertFalse(info.getQuotes().containsKey(SymbolEnum.USD));
    }
}


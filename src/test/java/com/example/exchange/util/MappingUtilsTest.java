package com.example.exchange.util;

import com.example.exchange.db.entity.SymbolExchangeRatesEntity;
import com.example.exchange.integration.exchangerate.dto.ExchangeRateResponseDto;
import com.example.exchange.integration.exchangerate.dto.SymbolEnum;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MappingUtilsTest {
    @Test
    void testMap_ValidResponse() {
        ExchangeRateResponseDto dto = new ExchangeRateResponseDto();
        dto.setSource("USD");
        dto.setTimestamp(123456789L);
        Map<String, Double> quotes = new HashMap<>();
        quotes.put("USDEUR", 0.9);
        quotes.put("USDGBP", 0.8);
        dto.setQuotes(quotes);

        SymbolExchangeRatesEntity entity = MappingUtils.map(dto);
        assertNotNull(entity);
        assertEquals(SymbolEnum.USD, entity.getSource());
        assertEquals(0.9, entity.getQuotes().get(SymbolEnum.EUR));
        assertEquals(0.8, entity.getQuotes().get(SymbolEnum.GBP));
    }

    @Test
    void testMap_EmptyQuotes() {
        ExchangeRateResponseDto dto = new ExchangeRateResponseDto();
        dto.setSource("USD");
        dto.setTimestamp(123456789L);
        dto.setQuotes(new HashMap<>());

        SymbolExchangeRatesEntity entity = MappingUtils.map(dto);
        assertNotNull(entity);
        assertTrue(entity.getQuotes().isEmpty());
    }
}


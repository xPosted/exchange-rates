package com.example.exchange.service;

import com.example.exchange.db.entity.SymbolExchangeRatesEntity;
import com.example.exchange.integration.exchangerate.dto.SymbolEnum;
import com.example.exchange.web.dto.ConvertionRequestDto;
import com.example.exchange.web.dto.ConvertionResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyConverterServiceTest {
    @Mock
    private ExchangeRatesService exchangeRatesService;
    @InjectMocks
    private CurrencyConverterService currencyConverterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvert_SingleRequest() {
        ConvertionRequestDto request = new ConvertionRequestDto(SymbolEnum.USD, SymbolEnum.EUR, 100D);

        Map<SymbolEnum, Double> quotes = new HashMap<>();
        quotes.put(SymbolEnum.EUR, 0.9);
        SymbolExchangeRatesEntity entity = SymbolExchangeRatesEntity.builder()
                .source(SymbolEnum.USD)
                .quotes(quotes)
                .build();

        when(exchangeRatesService.getExchangeRate(SymbolEnum.USD)).thenReturn(entity);

        ConvertionResponseDto response = currencyConverterService.convert(request);
        assertNotNull(response);
        assertEquals(SymbolEnum.EUR, response.getTarget());
        assertEquals(0.9, response.getRate());
        assertEquals(90.0, response.getAmount());
    }

    @Test
    void testConvert_MultipleRequests() {
        ConvertionRequestDto req1 = new ConvertionRequestDto(SymbolEnum.USD, SymbolEnum.EUR, 100D);
        ConvertionRequestDto req2 = new ConvertionRequestDto(SymbolEnum.USD, SymbolEnum.GBP, 200D);

        Map<SymbolEnum, Double> quotes = new HashMap<>();
        quotes.put(SymbolEnum.EUR, 0.9);
        quotes.put(SymbolEnum.GBP, 0.8);
        SymbolExchangeRatesEntity entity = SymbolExchangeRatesEntity.builder()
                .source(SymbolEnum.USD)
                .quotes(quotes)
                .build();

        when(exchangeRatesService.getExchangeRate(SymbolEnum.USD)).thenReturn(entity);

        List<ConvertionResponseDto> responses = currencyConverterService.convert(List.of(req1, req2));
        assertEquals(2, responses.size());
        assertEquals(SymbolEnum.EUR, responses.get(0).getTarget());
        assertEquals(90.0, responses.get(0).getAmount());
        assertEquals(SymbolEnum.GBP, responses.get(1).getTarget());
        assertEquals(160.0, responses.get(1).getAmount());
    }

    @Test
    void testConvert_TargetCurrencyNotFound() {
        ConvertionRequestDto request = new ConvertionRequestDto(SymbolEnum.USD, SymbolEnum.JPY, 100D);
        SymbolExchangeRatesEntity entity = SymbolExchangeRatesEntity.builder()
                .source(SymbolEnum.USD)
                .quotes(new HashMap<>())
                .build();

        when(exchangeRatesService.getExchangeRate(SymbolEnum.USD)).thenReturn(entity);

        assertThrows(IllegalArgumentException.class, () -> currencyConverterService.convert(request));
    }
}


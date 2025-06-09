package com.example.exchange.service;

import com.example.exchange.web.dto.ConvertionRequestDto;
import com.example.exchange.web.dto.ConvertionResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrencyConverterService {

    @Autowired
    private ExchangeRatesService exchangeRatesService;

    public ConvertionResponseDto convert(ConvertionRequestDto convertionRequestDto) {
        var source = convertionRequestDto.getSource();
        var target = convertionRequestDto.getTarget();
        var amount = convertionRequestDto.getAmount();
        var exchangeRates = exchangeRatesService.getExchangeRate(source);
        var targetRate = Optional.ofNullable(exchangeRates.getQuotes().get(target))
                .orElseThrow(() -> new IllegalArgumentException("Target currency not found in quotes: " + target));
        return ConvertionResponseDto.builder()
                .source(source)
                .target(target)
                .rate(targetRate)
                .amount(amount * targetRate)
                .build();
    }

    public List<ConvertionResponseDto> convert(List<ConvertionRequestDto> convertionRequestDtos) {
        return convertionRequestDtos.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

}

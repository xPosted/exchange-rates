package com.example.exchange.web;

import com.example.exchange.db.entity.SymbolExchangeRatesEntity;
import com.example.exchange.integration.exchangerate.dto.SymbolEnum;
import com.example.exchange.service.ClientService;
import com.example.exchange.service.CurrencyConverterService;
import com.example.exchange.service.ExchangeRatesService;
import com.example.exchange.web.dto.ConvertionRequestDto;
import com.example.exchange.web.dto.ConvertionResponseDto;
import com.example.exchange.web.dto.ExchangeRateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    @Autowired
    private CurrencyConverterService currencyConverterService;
    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/exchange-rates", produces = "application/json")
    public ExchangeRateInfo getExchangeRateInfo(@RequestParam SymbolEnum symbol,
                                                @RequestParam(required = false) SymbolEnum target) {
        return ofNullable(target)
                .map(t -> clientService.getExchangeRateInfo(symbol, target))
                .orElseGet(() -> clientService.getExchangeRateInfo(symbol));
    }

    @PostMapping(value = "convert", produces = "application/json")
    public ConvertionResponseDto convert(@RequestBody ConvertionRequestDto convertionRequestDto) {
        return currencyConverterService.convert(convertionRequestDto);
    }

    @PostMapping(value = "convert/batch", produces = "application/json")
    public List<ConvertionResponseDto> convert(@RequestBody List<ConvertionRequestDto> convertionRequestDtos) {
       return currencyConverterService.convert(convertionRequestDtos);
    }

}

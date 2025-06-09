package com.example.exchange.integration.exchangerate;

import com.example.exchange.integration.exchangerate.dto.ExchangeRateResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "exchangeRateClient", url = "https://api.exchangerate.host")
public interface ExchangeRateClient {
    @GetMapping("/live")
    ExchangeRateResponseDto getLiveRates(
            @RequestParam(value = "access_key") String access_key,
            @RequestParam(value = "source", required = false) String source,
            @RequestParam(value = "base", required = false) String base,
            @RequestParam(value = "symbols", required = false) String symbols);
}



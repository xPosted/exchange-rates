package com.example.exchange.web.dto;

import com.example.exchange.integration.exchangerate.dto.SymbolEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConvertionResponseDto {

    SymbolEnum source;
    SymbolEnum target;
    Double rate;
    Double amount;

}

package com.example.exchange.web.dto;

import com.example.exchange.integration.exchangerate.dto.SymbolEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertionRequestDto {

    SymbolEnum source;
    SymbolEnum target;
    Double amount;
}

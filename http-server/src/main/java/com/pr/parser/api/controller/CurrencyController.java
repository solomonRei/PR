package com.pr.parser.api.controller;

import com.pr.parser.api.dto.CurrencyDto;
import com.pr.parser.api.mappers.CurrencyMapper;
import com.pr.parser.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currency")
@Tag(name = "Currency API", description = "API for managing currencies")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    @Operation(summary = "Get all currencies", description = "Retrieves a list of all available currencies")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CurrencyDto> getAllCurrencies() {
        return currencyMapper.toDto(currencyService.getAll());
    }

    @Operation(summary = "Get currency by code", description = "Retrieves a currency by its unique code")
    @GetMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyDto getCurrencyByCode(
            @Parameter(description = "Currency code", required = true) @PathVariable String code) {
        return currencyMapper.toDto(currencyService.getByCode(code));
    }
}

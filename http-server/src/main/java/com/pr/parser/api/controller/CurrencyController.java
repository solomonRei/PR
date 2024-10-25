package com.pr.parser.api.controller;

import com.pr.parser.api.dto.CurrencyDto;
import com.pr.parser.mappers.CurrencyMapper;
import com.pr.parser.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currency")
public class CurrencyController {
    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CurrencyDto> getAllCurrencies() {
        return currencyMapper.toDto(currencyService.getAll());
    }

    @GetMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyDto getCurrencyByCode(String code) {
        return currencyMapper.toDto(currencyService.getByCode(code));
    }
}

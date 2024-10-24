package com.pr.parser.service;

import com.pr.parser.mappers.CurrencyMapper;
import com.pr.parser.model.CurrencyModel;
import com.pr.parser.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    public List<CurrencyModel> getAll() {
        return currencyMapper.toModel(currencyRepository.findAll());
    }

    public CurrencyModel getByCode(String code) {
        return getAll().stream()
                .filter(currency -> currency.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Currency not found: " + code));
    }
}

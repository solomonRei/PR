package com.pr.parser.api.validation;

import com.pr.parser.domain.model.Product;
import com.pr.parser.api.validation.rules.NameRule;
import com.pr.parser.api.validation.rules.PriceRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final NameRule nameValidator;

    private final PriceRule priceValidator;

    public void validate(Product product) {
        product.setName(nameValidator.validate(product.getName()));
        product.setPrice(priceValidator.validate(product.getPrice()));
    }
}

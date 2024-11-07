package com.pr.parser.api.validation.rules;

public interface ValidatorRule<T> {

    T validate(T value);
}

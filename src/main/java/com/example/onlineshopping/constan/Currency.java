package com.example.onlineshopping.constan;

public enum Currency {
    VND("VND");

    private final String value;

    Currency(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package com.bowie.financeering.user.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserCreateDTO {

    @NotNull
    @Pattern(regexp = "(BRL|USD|EUR)")
    private String currency;

    @NotBlank
    @Pattern(regexp = "([a-z]{2}[_]?[A-Z]{2}?)")
    private String locale;

    public UserCreateDTO() {
    }

    public UserCreateDTO(String currency, String locale) {
        this.currency = currency;
        this.locale = locale;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}

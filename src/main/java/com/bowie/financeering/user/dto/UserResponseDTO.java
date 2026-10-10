package com.bowie.financeering.user.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserResponseDTO {

    @Id
    private String userSub;

    @NotNull
    @Pattern(regexp = "(BRL|USD|EUR)")
    private String currency;

    @NotBlank
    @Pattern(regexp = "([a-z]{2}[_]?[A-Z]{2}?)")
    private String locale;

    public UserResponseDTO() {
    }

    public UserResponseDTO(String userSub, String currency, String locale) {
        this.userSub = userSub;
        this.currency = currency;
        this.locale = locale;
    }

    public String getUserSub() {
        return userSub;
    }

    public void setUserSub(String userSub) {
        this.userSub = userSub;
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

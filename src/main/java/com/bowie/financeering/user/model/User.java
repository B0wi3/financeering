package com.bowie.financeering.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String userSub;

    @NotNull
    @Pattern(regexp = "(BRL|USD|EUR)")
    @Column(name = "currency", length = 3)
    private String currency;

    @NotBlank
    @Pattern(regexp = "([a-z]{2}[_]?[A-Z]{2}?)")
    @Column(name = "locale")
    private String locale;
}

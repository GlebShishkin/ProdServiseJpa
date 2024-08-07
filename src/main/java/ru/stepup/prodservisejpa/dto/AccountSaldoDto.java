package ru.stepup.prodservisejpa.dto;

import java.math.BigDecimal;

public record AccountSaldoDto(String account, BigDecimal saldo) {
}

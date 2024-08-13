package ru.stepup.prodservisejpa.dto;

import java.math.BigDecimal;

public record PaymentDto (Long prodId, BigDecimal saldo) {
}

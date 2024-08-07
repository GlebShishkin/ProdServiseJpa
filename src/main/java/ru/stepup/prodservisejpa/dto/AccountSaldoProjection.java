package ru.stepup.prodservisejpa.dto;

import java.math.BigDecimal;

public interface AccountSaldoProjection {
    String getAccount();
    BigDecimal getSaldo();
}

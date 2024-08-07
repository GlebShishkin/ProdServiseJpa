package ru.stepup.prodservisejpa.dto;

import ru.stepup.prodservisejpa.enumerator.ProdType;

import java.math.BigDecimal;

public record ProductDto (Long id, Long userid, String account, BigDecimal saldo, ProdType typ) {
}

package ru.stepup.payservise.servise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.stepup.payservise.config.properties.ApplicationProperties;
import ru.stepup.payservise.dto.ResposeProductDto;
import ru.stepup.payservise.exceptions.IntegrationException;
import ru.stepup.prodservisejpa.dto.PaymentDto;
import ru.stepup.prodservisejpa.dto.AccountSaldoDto;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class PayProductServise {

    private RestTemplate restTemplate;
    private static ApplicationProperties applicationProperties;

    @Autowired
    public PayProductServise(RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

    // запрос продуктов пользователя
    public List<ResposeProductDto> getUserProducts(Long userId) {

        ResponseEntity<ResposeProductDto[]> response =
                restTemplate.getForEntity("/api/products?userId=" + userId, ResposeProductDto[].class);
        return Arrays.asList(response.getBody());
    }

    // проверка на доступность остатка
    // при успешной проверке вернем тек. остаток + номер счета
    public ResponseEntity<AccountSaldoDto> checkPay(PaymentDto paymentDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentDto> request = new HttpEntity<>(paymentDto, headers);
        ResponseEntity<AccountSaldoDto> accountSaldoDtoResponseEntity = restTemplate.postForEntity("/api/check", request, AccountSaldoDto.class);

        // при неуспешной проверке вызовем exception со статусом EXPECTATION_FAILED + вернем в сообщении тек. остаток + номер счета
        if (accountSaldoDtoResponseEntity.getBody().saldo().compareTo(paymentDto.saldo()) < 0)
        {
            throw new IntegrationException("Недостаточно средств на счете " + accountSaldoDtoResponseEntity.getBody().account()
                    + " для совершения платежа. Текущий остаток равен " + paymentDto.saldo()
                    , HttpStatus.EXPECTATION_FAILED);
        }
        return accountSaldoDtoResponseEntity;
    }

    public ResponseEntity<AccountSaldoDto> doPayment(PaymentDto paymentDto)  {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentDto> request = new HttpEntity<>(paymentDto, headers);
        return restTemplate.postForEntity("/api/dopay", request, AccountSaldoDto.class);
    }
}

package ru.stepup.prodservisejpa.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepup.prodservisejpa.dto.AccountSaldoDto;
import ru.stepup.prodservisejpa.dto.PaymentDto;
import ru.stepup.prodservisejpa.dto.ProductDto;
import ru.stepup.prodservisejpa.entity.Product;
import ru.stepup.prodservisejpa.exceptions.NotFoundException;
import ru.stepup.prodservisejpa.service.ProductService;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // получение всех продуктов пользователя
    @GetMapping("/product")
    public ProductDto getProductById(@RequestParam Long id) throws SQLException {
        Product product = productService.getProduct(id).orElseThrow(() -> new NotFoundException("Продукт с id = " + id + " не найден", HttpStatus.NOT_FOUND));
        return new ProductDto(product.getId(), product.getUser().getId(), product.getAccount(), product.getSaldo(), product.getTyp());
    }

    // получение всех продуктов пользователя
    @GetMapping("/products")
    public List<ProductDto> getUserProduct(@RequestParam Long userId) {
        return productService.getProducts(userId).stream().map(x -> new ProductDto(x.getId(), userId, x.getAccount(), x.getSaldo(), x.getTyp())).collect(Collectors.toList());
    }

    // проверка возможности платежа
    @PostMapping(value = "/check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountSaldoDto> checkPay(@RequestBody PaymentDto payDto) throws SQLException {
        return productService.checkPay(payDto.prodId(), payDto.saldo());
    }

    // совершение платежа
    @PostMapping(value = "/dopay", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountSaldoDto> doPayment(@RequestBody PaymentDto payDto) throws SQLException {
        return productService.doPaymet(payDto.prodId(), payDto.saldo());
    }
}

package ru.stepup.prodservisejpa.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.stepup.prodservisejpa.dto.AccountSaldoDto;
import ru.stepup.prodservisejpa.dto.AccountSaldoProjection;
import ru.stepup.prodservisejpa.entity.Product;
import ru.stepup.prodservisejpa.exceptions.InsufficientFundsException;
import ru.stepup.prodservisejpa.exceptions.NotFoundException;
import ru.stepup.prodservisejpa.repository.ProductRepo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Optional<Product> getProduct(Long id)  throws SQLException {
        return productRepo.findById(id);
    }

    public List<Product> getProducts(Long userId) {
        //return productRepo.findByUserid(userId);
        return productRepo.findByUser(userId);
    }

    // проверка совершения операции на счете (check)
    public ResponseEntity<AccountSaldoDto> checkPay(Long prodId, BigDecimal saldo) throws SQLException {

        Optional<AccountSaldoProjection> accSaldoProjection = productRepo.findAccountSaldoById(prodId);
        if (!accSaldoProjection.isPresent()) {
            throw new NotFoundException("Продукт с id = " + prodId + " не найден", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new AccountSaldoDto(accSaldoProjection.get().getAccount(), accSaldoProjection.get().getSaldo()), HttpStatus.OK);
    }

    // изменение остатка на счете
    @Transactional
    public ResponseEntity<AccountSaldoDto> doPaymet(Long prodId, BigDecimal saldo) throws SQLException {

        Optional<Product> optionalProduct = productRepo.findById(prodId);

        if (!optionalProduct.isPresent()) {
            throw new NotFoundException("Продукт с id = " + prodId + " не найден", HttpStatus.NOT_FOUND);
        } else {
            Product product = optionalProduct.get();
            if (product.getSaldo().compareTo(saldo) < 0) {
                throw new InsufficientFundsException("Недостаточно средств на счете " + product.getAccount(), HttpStatus.EXPECTATION_FAILED);
            }
            // меняем остаток на счете и возвращаем инфу о тек. остатке на счете
            product.setSaldo(product.getSaldo().subtract(saldo));
            productRepo.save(product);
            return new ResponseEntity<>(new AccountSaldoDto(product.getAccount(), product.getSaldo()), HttpStatus.OK);
        }
    }
}

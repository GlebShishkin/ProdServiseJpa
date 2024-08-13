package ru.stepup.prodservisejpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.stepup.prodservisejpa.dto.AccountSaldoProjection;
import ru.stepup.prodservisejpa.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long>  {

    // найти продукт по id продукта
    Optional<Product> findById(Long prodId);

    // найти продукты по id пользователя
    @Query(value = "SELECT * FROM products t where t.userid = ?1", nativeQuery = true)
    List<Product> findByUser(Long userId);

    // найти счет+остаток в виде проджекшена
    //            return new PaymentResponseDto(product.getAccount(), product.getSaldo().subtract(saldo));
    Optional<AccountSaldoProjection> findAccountSaldoById(Long prodId);
}

package ru.stepup.prodservisejpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.stepup.prodservisejpa.enumerator.ProdType;

import java.math.BigDecimal;
@Getter
@Setter
@Entity
@Table(name="products")
@AllArgsConstructor
@NoArgsConstructor
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "поле userid не может быть пустым")
    @Column(name="account")
    private String account;
    @Column(name="saldo")
    private BigDecimal saldo;
    @Enumerated(EnumType.STRING)    // значение будет сохранено в базу как строка
    @Column(name="typ")
    private ProdType typ;

/*
    @Column(name="userid")
    private Long userid;
*/


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User user;
}

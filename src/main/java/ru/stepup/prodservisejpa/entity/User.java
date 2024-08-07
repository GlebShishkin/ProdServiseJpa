package ru.stepup.prodservisejpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NamedEntityGraph(name = "User.with-products",
        attributeNodes = @NamedAttributeNode("products")
)

@Getter
@Setter
@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="username")
    private String userName;

    @OneToMany(mappedBy = "user")   // указываем имя поля в классе Product.user
//    private List<Product> products = new ArrayList<>();
    private Set<Product> products;

    public User(String userName) {
        this.userName = userName;
    }

}

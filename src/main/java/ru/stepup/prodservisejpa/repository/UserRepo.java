package ru.stepup.prodservisejpa.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.stepup.prodservisejpa.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    public Optional<User> findById(Long id);    //OK

    @EntityGraph(value="User.with-products")
    List<User> findAll();

    // ищем пользователей по началу наименования
    @EntityGraph(value="User.with-products")
    List<User> findByUserNameLike(String userName);

    // в JPQL нужно обращаьбся к объекту (User) а не к таблице (users), иначе получается ошибка: Validation failed for query for method
    @EntityGraph(value="User.with-products")
    @Query("select u from User u where u.id = ?1")
    User findByUserId(Long userid);
}

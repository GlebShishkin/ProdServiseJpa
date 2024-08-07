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

/*  ????????????????????????
    // дает ошибку: Validation failed for query for method
    @EntityGraph(value="User.with-products")
    @Query("select * from users u where u.id = ?1")
    User findByUserId(Long userid);
*/

    @Query(value = "select * from users u where u.id = ?1", nativeQuery = true)
    User findByUserId(Long userid);

}

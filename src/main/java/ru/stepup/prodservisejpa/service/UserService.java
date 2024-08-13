package ru.stepup.prodservisejpa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.stepup.prodservisejpa.dto.UserDto;
import ru.stepup.prodservisejpa.entity.User;
import ru.stepup.prodservisejpa.repository.UserRepo;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("UserService")
public class UserService implements CommandLineRunner {

    private UserRepo userRepo;
    @Autowired
    public UserService( UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Long save (User user) {
       return userRepo.save(user).getId();
    }

    public Optional<User> getUser(Long id) throws SQLException {
        return userRepo.findById(id);
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    // поиск пользователей по началу имени
    public List<UserDto> getUsersLike(String userName) {
        return userRepo.findByUserNameLike(userName + "%").stream().map(x -> new UserDto(x.getId(), x.getUserName())).toList();
    }

    @Override
    public void run(String... args) throws Exception {
        //  log.info(userRepo.findByUserNameLike("Ivanov" + "%").toString());
        //  log.info(userRepo.findByUserId(1L).getUserName());
    }
}



package ru.stepup.prodservisejpa.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.stepup.prodservisejpa.dto.UserDto;
import ru.stepup.prodservisejpa.entity.User;
import ru.stepup.prodservisejpa.exceptions.NotFoundException;
import ru.stepup.prodservisejpa.service.UserService;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // добавление пользователя
    @PostMapping(value = "/adduser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Long addUser(@RequestBody UserDto userDto) {
        log.info("userDto.userName() = " + userDto.userName());
        User user = new User(userDto.userName());
        return userService.save(user);
    }

    // спискок всех пользоватей
    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userService.getUsers().stream().map(x -> new UserDto(x.getId(), x.getUserName())).collect(Collectors.toList());
    }

    // поиск пользоватея по id
    @GetMapping("/user")
    public UserDto getUserById(@RequestParam Long id) throws SQLException {
        User user = userService.getUser(id).orElseThrow(() -> new NotFoundException("Пользователь  с id = " + id + " не найден", HttpStatus.NOT_FOUND));
        return new UserDto(user.getId(), user.getUserName());
    }

    // поиск пользователей по имени
    @GetMapping("/find_users")
    public List<UserDto> getUsersByName(@RequestParam String userName) {
        log.info("######### getUsersByName: count = " + userService.getUsersLike(userName).stream().count());
        return userService.getUsersLike(userName);
        }
}

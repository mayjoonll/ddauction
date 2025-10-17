package com.my.backend.controller;

import com.my.backend.dto.UserDto;
import com.my.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    // 회원가입
    @PostMapping("/signup")
    public UserDto signup(@Valid @RequestBody UserDto dto) {
        return userService.signup(dto);
    }

    // 로그인
    @PostMapping("/login")
    public UserDto login(@RequestBody UserDto dto) {
        return userService.login(dto);
    }

    // 정보수정
    @PutMapping
    public UserDto updateUser(@RequestBody UserDto dto) {
        return userService.updateUser(dto);
    }

    // 삭제
    @DeleteMapping
    public void deleteUser(@RequestBody UserDto dto) {
        userService.deleteUser(dto.getUserId());
    }
}

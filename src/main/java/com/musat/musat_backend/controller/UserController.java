package com.musat.musat_backend.controller;

import com.musat.musat_backend.dto.request.UserDto;
import com.musat.musat_backend.dto.response.UserResponse;
import com.musat.musat_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 모든 사용자 조회
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 특정 사용자 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // 사용자 등록
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserDto request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    // 사용자 수정
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Integer id,
            @RequestBody UserDto request
    ) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

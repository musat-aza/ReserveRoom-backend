package com.musat.musat_backend.service;

import com.musat.musat_backend.dto.request.UserDto;
import com.musat.musat_backend.dto.response.UserResponse;
import com.musat.musat_backend.entity.User;
import com.musat.musat_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 모든 사용자 조회
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 사용자 상세 조회
    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + id));
        return UserResponse.fromEntity(user);
    }

    // 사용자 등록
    public UserResponse createUser(UserDto request) {
        User saved = userRepository.save(request.toEntity());
        return UserResponse.fromEntity(saved);
    }

    // 사용자 수정
    public UserResponse updateUser(Integer id, UserDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + id));

        user.setName(request.getName());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());

        User updated = userRepository.save(user);
        return UserResponse.fromEntity(updated);
    }

    // 사용자 삭제
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다. ID: " + id);
        }
        userRepository.deleteById(id);
    }
}

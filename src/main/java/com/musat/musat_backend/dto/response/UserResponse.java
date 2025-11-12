package com.musat.musat_backend.dto.response;

import com.musat.musat_backend.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Integer id;
    private String name;
    private String email;
    private LocalDateTime joinedAt;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .joinedAt(user.getJoinedAt())
                .build();
    }
}

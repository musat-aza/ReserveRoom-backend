package com.musat.musat_backend.dto.request;

import com.musat.musat_backend.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String name;
    private String password;
    private String email;

    public User toEntity() {
        return User.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();
    }
}

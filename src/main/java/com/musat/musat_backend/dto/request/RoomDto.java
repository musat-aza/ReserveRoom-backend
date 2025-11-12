package com.musat.musat_backend.dto.request;

import com.musat.musat_backend.entity.Room;
import com.musat.musat_backend.entity.RoomType;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor()
@AllArgsConstructor
@Builder
public class RoomDto {
    private String name;
    private RoomType type;
    private Integer capacity;

    public Room toEntity() {
        return Room.builder()
                .name(name)
                .type(type)
                .capacity(capacity)
                .build();
    }
}

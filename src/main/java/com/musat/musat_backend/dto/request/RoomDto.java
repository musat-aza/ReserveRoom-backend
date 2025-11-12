package com.musat.musat_backend.dto.request;

import com.musat.musat_backend.entity.Room;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDto {
    private String name;
    private String type;
    private Integer capacity;

    public Room toEntity() {
        return Room.builder()
                .name(name)
                .type(type)
                .capacity(capacity)
                .build();
    }
}

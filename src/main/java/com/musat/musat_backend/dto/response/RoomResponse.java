package com.musat.musat_backend.dto.response;

import com.musat.musat_backend.entity.Room;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private Integer id;
    private String name;
    private String type;
    private Integer capacity;

    public static RoomResponse fromEntity(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .type(room.getType())
                .capacity(room.getCapacity())
                .build();
    }
}

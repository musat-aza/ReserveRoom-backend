package com.musat.musat_backend.service;

import com.musat.musat_backend.dto.request.RoomDto;
import com.musat.musat_backend.dto.response.RoomResponse;
import com.musat.musat_backend.entity.Room;
import com.musat.musat_backend.entity.RoomType;
import com.musat.musat_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    // 전체 회의실 조회
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(RoomResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 종류의 회의실 조회 (스매시룸 / 큐브)
    public List<RoomResponse> getRoomsByType(RoomType type) {
        return roomRepository.findByType(type).stream()
                .map(RoomResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 회의실 단건 조회
    public RoomResponse getRoomById(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회의실을 찾을 수 없습니다. ID: " + id));
        return RoomResponse.fromEntity(room);
    }

    // 회의실 생성
    public RoomResponse createRoom(RoomDto request) {
        Room saved = roomRepository.save(request.toEntity());
        return RoomResponse.fromEntity(saved);
    }

    // 회의실 수정
    public RoomResponse updateRoom(Integer id, RoomDto request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회의실을 찾을 수 없습니다. ID: " + id));

        room.setName(request.getName());
        room.setType(request.getType());
        room.setCapacity(request.getCapacity());

        Room updated = roomRepository.save(room);
        return RoomResponse.fromEntity(updated);
    }

    // 회의실 삭제
    public void deleteRoom(Integer id) {
        if (!roomRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 회의실입니다. ID: " + id);
        }
        roomRepository.deleteById(id);
    }
}

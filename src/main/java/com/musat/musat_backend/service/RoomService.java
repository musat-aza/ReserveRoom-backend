package com.musat.musat_backend.service;

import com.musat.musat_backend.dto.request.RoomDto;
import com.musat.musat_backend.dto.response.RoomResponse;
import com.musat.musat_backend.entity.Room;
import com.musat.musat_backend.entity.RoomType;
import com.musat.musat_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
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

    // [수정됨] 특정 타입의 사용 중인 방 ID 목록 반환
    public List<Integer> getActiveRoomIds(String typeStr) {
        // 1. 문자열(cube) -> Enum(CUBE) 변환 (대소문자 처리)
        // 예: "cube" -> "CUBE"로 변환되어 RoomType.CUBE를 찾음
        RoomType type;
        try {
            type = RoomType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 회의실 종류입니다: " + typeStr);
        }

        // 2. 현재 시간 기준 조회
        return roomRepository.findActiveRoomIdsByType(LocalDateTime.now(), type);
    }
}

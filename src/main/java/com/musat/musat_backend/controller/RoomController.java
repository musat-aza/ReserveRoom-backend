package com.musat.musat_backend.controller;

import com.musat.musat_backend.dto.request.RoomDto;
import com.musat.musat_backend.dto.response.RoomResponse;
import com.musat.musat_backend.entity.RoomType;
import com.musat.musat_backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    /**
     * 전체 회의실 조회 or 종류별 필터링
     * 예시:
     *  - GET /api/rooms → 전체
     *  - GET /api/rooms?type=cube → 큐브만
     *  - GET /api/rooms?type=smash → 스매시룸만
     */
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getRooms(@RequestParam(required = false) RoomType type) {
        if (type != null) {
            return ResponseEntity.ok(roomService.getRoomsByType(type));
        }
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Integer id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // 생성
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomDto request) {
        return ResponseEntity.ok(roomService.createRoom(request));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Integer id,
            @RequestBody RoomDto request
    ) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}

package com.musat.musat_backend.controller;

import com.musat.musat_backend.dto.request.RoomDto;
import com.musat.musat_backend.dto.response.RoomResponse;
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

    // 모든 회의실 조회
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // 특정 회의실 조회
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Integer id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // 회의실 등록
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomDto request) {
        return ResponseEntity.ok(roomService.createRoom(request));
    }

    // 회의실 수정
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Integer id,
            @RequestBody RoomDto request
    ) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    // 회의실 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}

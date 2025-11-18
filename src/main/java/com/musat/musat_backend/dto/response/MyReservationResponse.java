package com.musat.musat_backend.dto.response;

import lombok.Getter;
import java.util.List;

@Getter
public class MyReservationResponse {

    // 1. 요약 건수
    private long totalCount;
    private long cubeCount;
    private long smashCount;

    // 2. 예약 목록
    private List<ReservationResponse> reservations; // 기존 ReservationResponse 재활용

    public MyReservationResponse(long totalCount, long cubeCount, long smashCount, List<ReservationResponse> reservations) {
        this.totalCount = totalCount;
        this.cubeCount = cubeCount;
        this.smashCount = smashCount;
        this.reservations = reservations;
    }
}
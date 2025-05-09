package com.pool.poolcrud.DTO.Reservation;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationIntervalRequest {
    @NotNull
    private Long clientId;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime start;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime end;

    public ReservationIntervalRequest(Long clientId, LocalDate date, LocalTime start, LocalTime end) {
        this.clientId = clientId;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
}

package com.pool.poolcrud.DTO.Reservation;

import java.time.LocalDateTime;

public class ReservationRequestDTO {
    private Long clientId;
    private LocalDateTime dateTime;

    public ReservationRequestDTO(Long clientId, LocalDateTime dateTime) {
        this.clientId = clientId;
        this.dateTime = dateTime;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}

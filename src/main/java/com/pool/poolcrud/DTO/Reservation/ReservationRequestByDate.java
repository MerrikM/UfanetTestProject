package com.pool.poolcrud.DTO.Reservation;

import java.time.LocalDate;

public class ReservationRequestByDate {
    private Long clientId;
    private LocalDate reservationDate;

    public ReservationRequestByDate(Long clientId, LocalDate reservationDate) {
        this.clientId = clientId;
        this.reservationDate = reservationDate;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }
}

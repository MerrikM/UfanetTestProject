package com.pool.poolcrud.DTO.Reservation;

public class ConfirmReservationDTO {
    private String orderId;

    public ConfirmReservationDTO(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

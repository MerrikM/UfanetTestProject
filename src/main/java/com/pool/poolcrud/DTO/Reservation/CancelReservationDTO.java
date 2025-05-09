package com.pool.poolcrud.DTO.Reservation;

public class CancelReservationDTO {
    private Long clientId;
    private String orderId;

    public CancelReservationDTO(Long clientId, String orderId) {
        this.clientId = clientId;
        this.orderId = orderId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

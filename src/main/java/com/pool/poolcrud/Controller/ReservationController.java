package com.pool.poolcrud.Controller;

import com.pool.poolcrud.Model.Reservation;
import com.pool.poolcrud.Service.ReservationService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/v0/pool/timetable/")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/{poolId}/reserve")
    public ResponseEntity<Reservation> reserve(@PathVariable("poolId") Long poolId,
                                               @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.reserve(
                reservationRequest.getClientId(),
                poolId,
                reservationRequest.getDateTime()
        );

        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/{poolId}/cancel")
    public ResponseEntity<String> cancel(@PathVariable("poolId") Long poolId,
                                              @RequestBody CancelRequest cancelRequest) {
        reservationService.cancel(
                cancelRequest.getClientId(),
                cancelRequest.getOrderId()
        );

        return ResponseEntity.ok("Запись успешно отменена");
    }

    public static class ReservationRequest {
        private Long clientId;
        private LocalDateTime dateTime;

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

    public static class CancelRequest {
        private Long clientId;
        private String orderId;

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
}

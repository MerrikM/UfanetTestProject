package com.pool.poolcrud.Controller;

import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Model.Reservation;
import com.pool.poolcrud.Service.PoolService;
import com.pool.poolcrud.Service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v0/pool/timetable/")
public class ReservationController {

    private final ReservationService reservationService;
    private final PoolService poolService;

    public ReservationController(ReservationService reservationService, PoolService poolService) {
        this.reservationService = reservationService;
        this.poolService = poolService;
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

    @GetMapping("/{poolId}/getByFIO")
    public ResponseEntity<List<Reservation>> getReservationByFIO(@PathVariable("poolId") Long poolId,
                                                                      @RequestBody ReservationByFIORequest reservationRequest) {
        List<Reservation> reservationList = reservationService.getByFIO(
                reservationRequest.getName(),
                reservationRequest.getSurname(),
                reservationRequest.getPatronymic()
        );
        // Pool pool = poolService.getPoolById(poolId);
        return ResponseEntity.ok(reservationList);
    }


    //public ResponseEntity<List<Reservation>> getReservationByDate();

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

    public static class ReservationByFIORequest {
        private String name;
        private String surname;
        private String patronymic;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getPatronymic() {
            return patronymic;
        }

        public void setPatronymic(String patronymic) {
            this.patronymic = patronymic;
        }
    }
}

package com.pool.poolcrud.Controller;

import com.pool.poolcrud.DTO.Reservation.*;
import com.pool.poolcrud.Model.Reservation;
import com.pool.poolcrud.Service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v0/pool/timetable/")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/{poolId}/reserve")
    public ResponseEntity<?> reserve(@PathVariable("poolId") Long poolId,
                                     @RequestBody ReservationRequestDTO reservationRequestDTO) {
        try {
            Reservation reservation = reservationService.reserve(
                    reservationRequestDTO.getClientId(),
                    poolId,
                    reservationRequestDTO.getDateTime()
            );
            return ResponseEntity.ok(reservation);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{poolId}/reserve-interval")
    public ResponseEntity<?> reserveInterval( @PathVariable("poolId") Long poolId,
                                              @RequestBody ReservationIntervalRequest request) {
        try {
            List<Reservation> reservations = reservationService.reserveForAFewHours(
                    request.getClientId(),
                    poolId,
                    request.getDate(),
                    request.getStart(),
                    request.getEnd()
            );
            return ResponseEntity.ok(reservations);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{poolId}/cancel")
    public ResponseEntity<?> cancel(@PathVariable("poolId") Long poolId,
                                    @RequestBody CancelReservationDTO reservationDTO) {
        try {
            reservationService.cancel(
                    reservationDTO.getClientId(),
                    reservationDTO.getOrderId()
            );
            return ResponseEntity.ok("Запись успешно отменена");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{poolId}/getByFIO")
    public ResponseEntity<?> getReservationByFIO(@PathVariable("poolId") Long poolId,
                                                 @RequestBody ReservationByFIODTO reservationDTO) {
        try {
            List<Reservation> reservationList = reservationService.getByFIO(
                    reservationDTO.getName(),
                    reservationDTO.getSurname(),
                    reservationDTO.getPatronymic()
            );

            return ResponseEntity.ok(reservationList);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{poolId}/getByDate")
    public ResponseEntity<List<Reservation>> getReservationByDate(@PathVariable("poolId") Long poolId,
                                                                  @RequestBody ReservationRequestByDate reservationDTO) {
        List<Reservation> reservations = reservationService.getByReservationDay(
                reservationDTO.getClientId(),
                reservationDTO.getReservationDate()
        );
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/{poolId}/confirm")
    public ResponseEntity<?> confirmVisit(@PathVariable("poolId") Long poolId,
                                          @RequestBody ConfirmReservationDTO reservationDTO) {
        try {
            reservationService.confirmVisit(reservationDTO.getOrderId());
            return ResponseEntity.ok("Посещение успешно подтверждено");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

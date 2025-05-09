package com.pool.poolcrud.Controller;

import com.pool.poolcrud.DTO.HolydayWorkSchedule.HolidayWorkScheduleDTO;
import com.pool.poolcrud.Model.HolidayWorkSchedule;
import com.pool.poolcrud.Service.HolidayWorkScheduleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v0/pool/timetable/holiday/")
public class HolidayWorkScheduleController {

    private final HolidayWorkScheduleService holidayWorkScheduleService;

    public HolidayWorkScheduleController(HolidayWorkScheduleService holidayWorkScheduleService) {
        this.holidayWorkScheduleService = holidayWorkScheduleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHolidayById(@PathVariable("id") Long id) {
        try {
            HolidayWorkSchedule holiday = holidayWorkScheduleService.getHolidayWorkScheduleById(id);
            return ResponseEntity.ok(holiday);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllHolidays() {
        try {
            List<HolidayWorkSchedule> holidays = holidayWorkScheduleService.findAll();
            return ResponseEntity.ok(holidays);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addHoliday(@RequestBody HolidayWorkScheduleDTO holidayWorkScheduleDTO) {
        try {
            holidayWorkScheduleService.addHoliday(
                    holidayWorkScheduleDTO.getDate(),
                    holidayWorkScheduleDTO.getDescription(),
                    holidayWorkScheduleDTO.getOpenTime(),
                    holidayWorkScheduleDTO.getCloseTime()
            );
            return ResponseEntity.ok("Праздник успешно добавлен");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateHoliday(@PathVariable Long id,
                                           @RequestBody HolidayWorkScheduleDTO updateDTO) {
        try {
            holidayWorkScheduleService.updateHoliday(
                    id,
                    updateDTO
            );
            return ResponseEntity.ok("Праздник успешно обновлен");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> removeHoliday(@RequestParam LocalDate date) {
        try {
            holidayWorkScheduleService.removeHoliday(date);
            return ResponseEntity.ok("Праздник успешно удален");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> isHoliday(@RequestParam LocalDate date) {
        HolidayWorkSchedule holiday = holidayWorkScheduleService.isHoliday(date);
        if (holiday != null) {
            return ResponseEntity.ok(holiday);
        } else {
            return ResponseEntity.ok("На указанную дату праздник не запланирован");
        }
    }
}

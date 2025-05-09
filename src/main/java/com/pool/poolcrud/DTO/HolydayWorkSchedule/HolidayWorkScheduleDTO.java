package com.pool.poolcrud.DTO.HolydayWorkSchedule;

import java.time.LocalDate;
import java.time.LocalTime;

public class HolidayWorkScheduleDTO {
    private LocalDate date;
    private String description;
    private LocalTime openTime;
    private LocalTime closeTime;

    public HolidayWorkScheduleDTO(LocalDate date, String description, LocalTime openTime, LocalTime closeTime) {
        this.date = date;
        this.description = description;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }
}

package com.pool.poolcrud.DTO;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pool.poolcrud.Model.Reservation;
import com.pool.poolcrud.Model.TimeTable;
import com.pool.poolcrud.Model.WorkSchedule;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class PoolDTO {

    @NotBlank
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private int maxCapacity;

    @NotBlank
    private int maxVisitsPerDay;

    public PoolDTO(String name, int maxCapacity, int maxVisitsPerDay) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.maxVisitsPerDay = maxVisitsPerDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getMaxVisitsPerDay() {
        return maxVisitsPerDay;
    }

    public void setMaxVisitsPerDay(int maxVisitsPerDay) {
        this.maxVisitsPerDay = maxVisitsPerDay;
    }
}

package com.pool.poolcrud.DTO.Pool;

public class PoolCreateRequest {
    private String name;
    private int maxCapacity;
    private int maxVisitsPerDay;

    public PoolCreateRequest(String name, int maxCapacity, int maxVisitsPerDay) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.maxVisitsPerDay = maxVisitsPerDay;
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

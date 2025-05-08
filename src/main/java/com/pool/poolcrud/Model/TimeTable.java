package com.pool.poolcrud.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "timetable")
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "pool_id", nullable = false)
    @JsonBackReference
    private Pool pool;

    @Column(nullable = false)
    private int currentBookings = 0;

    @Column(nullable = false)
    private int remainingCapacity;

    public TimeTable() {}

    public TimeTable(LocalDate date, LocalTime time, Pool pool, int currentBookings, int remainingCapacity) {
        this.date = date;
        this.time = time;
        this.pool = pool;
        this.currentBookings = currentBookings;
        this.remainingCapacity = remainingCapacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public int getCurrentBookings() {
        return currentBookings;
    }

    public void setCurrentBookings(int currentBookings) {
        this.currentBookings = currentBookings;
    }

    public int getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(int remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }
}

package com.pool.poolcrud.Model;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "workschedule")
// График работы
public class WorkSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pool_id", nullable = false)
    private Pool pool;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private boolean dayOf;

    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    public WorkSchedule() {}

    public WorkSchedule(Pool pool, LocalTime openTime, LocalTime closeTime, boolean dayOf, DayOfWeek dayOfWeek) {
        this.pool = pool;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.dayOf = dayOf;
        this.dayOfWeek = dayOfWeek;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
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

    public boolean isDayOf() {
        return dayOf;
    }

    public void setDayOf(boolean dayOf) {
        this.dayOf = dayOf;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}

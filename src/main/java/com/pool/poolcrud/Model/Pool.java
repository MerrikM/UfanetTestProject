package com.pool.poolcrud.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pool")
public class Pool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int maxCapacity;

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<TimeTable> timeTables;

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL)
    private List<WorkSchedule> workSchedules;

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Reservation> reservations;

    public Pool() {}

    public Pool(String name, int maxCapacity) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.timeTables = new ArrayList<>();
        this.reservations = new ArrayList<>();
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

    public List<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(List<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }

    public List<WorkSchedule> getWorkSchedules() {
        return workSchedules;
    }

    public void setWorkSchedules(List<WorkSchedule> workSchedules) {
        this.workSchedules = workSchedules;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}

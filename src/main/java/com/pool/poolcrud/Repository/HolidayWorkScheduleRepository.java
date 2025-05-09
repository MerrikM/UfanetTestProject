package com.pool.poolcrud.Repository;

import com.pool.poolcrud.Model.HolidayWorkSchedule;
import com.pool.poolcrud.Service.HolidayWorkScheduleService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HolidayWorkScheduleRepository extends JpaRepository<HolidayWorkSchedule, Long> {
    HolidayWorkSchedule findByDate(LocalDate date);

    void deleteByDate(LocalDate date);
}

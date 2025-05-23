package com.pool.poolcrud.Repository;

import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
    List<TimeTable> findByPoolIdAndCurrentBookingsLessThan(Long poolId, int maxBookings);

    List<TimeTable> findByPoolIdAndDate(Long poolId, LocalDate date);
    TimeTable findByPoolAndDateAndTime(Pool pool, LocalDate date, LocalTime time);
    List<TimeTable> findByPoolAndDateAndTimeBetween(Pool pool, LocalDate date, LocalTime start, LocalTime end);

    void deleteByDate(LocalDate date);
}

package com.pool.poolcrud.Repository;

import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Model.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {
    List<WorkSchedule> findByPool(Pool pool);
    WorkSchedule findByPoolIdAndDayOfWeek(Long poolId, DayOfWeek dayOfWeek);
}

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
    Optional<WorkSchedule> findByPoolId(Long poolId);
    List<WorkSchedule> findByPool(Pool pool);
    void deleteByPool(Pool pool);
    boolean existsByPoolAndDayOfWeek(Pool pool, DayOfWeek dayOfWeek);
    WorkSchedule findByPoolIdAndDayOfWeek(Long poolId, DayOfWeek dayOfWeek);
}

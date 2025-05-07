package com.pool.poolcrud.Service;

import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Model.WorkSchedule;
import com.pool.poolcrud.Repository.WorkScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Service
public class WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;

    public WorkScheduleService(WorkScheduleRepository workScheduleRepository) {
        this.workScheduleRepository = workScheduleRepository;
    }

    @Transactional
    public void setupDefaultWorkSchedule(Pool pool) {
        for (DayOfWeek day : DayOfWeek.values()) {
            WorkSchedule workSchedule = new WorkSchedule();
            workSchedule.setPool(pool);
            workSchedule.setDayOfWeek(day);
            if (day == DayOfWeek.SUNDAY) {
                workSchedule.setHoliday(true);
            }
            workSchedule.setOpenTime(LocalTime.of(8, 0));
            workSchedule.setCloseTime(LocalTime.of(21, 0));

            workScheduleRepository.save(workSchedule);
        }
    }
}

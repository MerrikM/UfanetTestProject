package com.pool.poolcrud.Service;

import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Model.TimeTable;
import com.pool.poolcrud.Model.WorkSchedule;
import com.pool.poolcrud.Repository.TimeTableRepository;
import com.pool.poolcrud.Repository.WorkScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final PoolService poolService;
    private final WorkScheduleRepository workScheduleRepository;

    public TimeTableService(TimeTableRepository timeTableRepository, PoolService poolService, WorkScheduleRepository workScheduleRepository) {
        this.timeTableRepository = timeTableRepository;
        this.poolService = poolService;
        this.workScheduleRepository = workScheduleRepository;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAvailable(Long poolId, LocalDate date) {
        Pool pool = poolService.getPoolById(poolId);
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        WorkSchedule workSchedule = workScheduleRepository.findByPoolIdAndDayOfWeek(poolId, dayOfWeek);

        if (workSchedule.isHoliday()) {
            return null;
        }

        List<TimeTable> timeTableList = timeTableRepository.findByPoolIdAndDate(poolId, date);
        List<Map<String, Object>> availableTime = new ArrayList<>();

        for (int i = 0; i < timeTableList.size() - 1; i++) {
            int available = pool.getMaxCapacity() - timeTableList.get(i).getCurrentBookings();
            if (available > 0) {
                Map<String, Object> availableSlot = new HashMap<>();
                availableSlot.put("time", timeTableList.get(i).getTime().toString());
                availableSlot.put("count", available);
                availableTime.add(availableSlot);
            }
        }

        return availableTime;
    }


}

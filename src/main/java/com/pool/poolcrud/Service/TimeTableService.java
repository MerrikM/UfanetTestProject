package com.pool.poolcrud.Service;

import com.pool.poolcrud.Controller.ClientController;
import com.pool.poolcrud.Model.Client;
import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Model.TimeTable;
import com.pool.poolcrud.Model.WorkSchedule;
import com.pool.poolcrud.Repository.PoolRepository;
import com.pool.poolcrud.Repository.TimeTableRepository;
import com.pool.poolcrud.Repository.WorkScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final PoolRepository poolRepository;
    private final WorkScheduleRepository workScheduleRepository;

    public TimeTableService(TimeTableRepository timeTableRepository, PoolRepository poolRepository, WorkScheduleRepository workScheduleRepository) {
        this.timeTableRepository = timeTableRepository;
        this.poolRepository = poolRepository;
        this.workScheduleRepository = workScheduleRepository;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAvailable(Long poolId, LocalDate date) {
        Pool pool = poolRepository.findById(poolId).orElseThrow(() -> new RuntimeException("Запись не найдена в БД"));
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        WorkSchedule workSchedule = workScheduleRepository.findByPoolIdAndDayOfWeek(poolId, dayOfWeek);

        if (workSchedule.isHoliday()) {
            return null;
        }

        List<TimeTable> timeTableList = timeTableRepository.findByPoolIdAndDate(poolId, date);
        List<Map<String, Object>> availableTime = new ArrayList<>();

        for (int i = 0; i < timeTableList.size() - 1; i++) {
            int available = timeTableList.get(i).getRemainingCapacity() - timeTableList.get(i).getCurrentBookings();
            if (available > 0) {
                Map<String, Object> availableSlot = new HashMap<>();
                availableSlot.put("time", timeTableList.get(i).getTime().toString());
                availableSlot.put("count", available);
                availableTime.add(availableSlot);
            }
        }

        return availableTime;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getOccupied(Long poolId, LocalDate date) {
        Pool pool = poolRepository.findById(poolId)
                .orElseThrow(() -> new RuntimeException("Запись не найдена в БД"));
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        WorkSchedule workSchedule = workScheduleRepository.findByPoolIdAndDayOfWeek(poolId, dayOfWeek);

        if (workSchedule.isHoliday()) {
            return Collections.emptyList(); // Возвращаем пустой список вместо null
        }

        List<TimeTable> timeTableList = timeTableRepository.findByPoolIdAndDate(poolId, date);
        List<Map<String, Object>> occupiedTime = new ArrayList<>();

        for (TimeTable timeSlot : timeTableList) {
            int occupied = timeSlot.getCurrentBookings();
            if (occupied > 0) {
                Map<String, Object> occupiedSlot = new HashMap<>();
                occupiedSlot.put("time", timeSlot.getTime().toString());
                occupiedSlot.put("count", occupied);
                occupiedTime.add(occupiedSlot);
            }
        }

        return occupiedTime;
    }

    @Transactional
    public void generateTimeTable(Long poolId, LocalDate startDate, LocalDate endDate) {
        Pool pool = poolRepository.findById(poolId).orElseThrow(() -> new RuntimeException("Запись не найдена в БД"));
        List<WorkSchedule> workScheduleList = workScheduleRepository.findByPool(pool);
        List<TimeTable> slots = new ArrayList<>();

        Map<DayOfWeek, WorkSchedule> scheduleMap = new HashMap<>();
        for (int i = 0; i < workScheduleList.size(); i++) {
            scheduleMap.put(
                    workScheduleList.get(i).getDayOfWeek(),
                    workScheduleList.get(i)
            );
        }

        for (LocalDate date = startDate; date.isAfter(endDate) == false; date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            WorkSchedule schedule = scheduleMap.get(dayOfWeek);
            if (schedule == null) {
                throw new IllegalStateException("Нет расписания для дня: " + dayOfWeek);
            }

            if (schedule.isHoliday() == true) {
                continue;
            }

            generateTableForDay(pool, date, schedule, slots);
        }
        timeTableRepository.saveAll(slots);
    }

    @Transactional
    public void generateTableForDay(Pool pool, LocalDate date, WorkSchedule workSchedule, List<TimeTable> timeTableList) {
        for (LocalTime currentTime = workSchedule.getOpenTime();
             currentTime.isBefore(workSchedule.getCloseTime());
             currentTime = currentTime.plusHours(1)) {

            TimeTable slot = new TimeTable(
                    date,
                    currentTime,
                    pool,
                    0,
                    pool.getMaxCapacity() / 12
            );

            timeTableList.add(slot);
        }
    }
}

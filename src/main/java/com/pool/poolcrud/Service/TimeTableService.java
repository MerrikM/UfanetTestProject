package com.pool.poolcrud.Service;

import com.pool.poolcrud.Controller.ClientController;
import com.pool.poolcrud.Model.*;
import com.pool.poolcrud.Repository.HolidayWorkScheduleRepository;
import com.pool.poolcrud.Repository.PoolRepository;
import com.pool.poolcrud.Repository.TimeTableRepository;
import com.pool.poolcrud.Repository.WorkScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.action.internal.EntityActionVetoException;
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
    private final HolidayWorkScheduleRepository holidayWorkScheduleRepository;

    public TimeTableService(TimeTableRepository timeTableRepository, PoolRepository poolRepository, WorkScheduleRepository workScheduleRepository, HolidayWorkScheduleRepository holidayWorkScheduleRepository) {
        this.timeTableRepository = timeTableRepository;
        this.poolRepository = poolRepository;
        this.workScheduleRepository = workScheduleRepository;
        this.holidayWorkScheduleRepository = holidayWorkScheduleRepository;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAvailable(Long poolId, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        WorkSchedule workSchedule = workScheduleRepository.findByPoolIdAndDayOfWeek(poolId, dayOfWeek);

        if (workSchedule == null || workSchedule.isDayOf()) {
            return Collections.emptyList();
        }

        List<TimeTable> timeTableList = timeTableRepository.findByPoolIdAndDate(poolId, date);
        List<Map<String, Object>> availableTime = new ArrayList<>();

        for (int i = 0; i < timeTableList.size(); i++) {
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
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        WorkSchedule workSchedule = workScheduleRepository.findByPoolIdAndDayOfWeek(poolId, dayOfWeek);

        if (workSchedule == null || workSchedule.isDayOf()) {
            return Collections.emptyList();
        }

        List<TimeTable> timeTableList = timeTableRepository.findByPoolIdAndDate(poolId, date);
        List<Map<String, Object>> occupiedTime = new ArrayList<>();

        for (int i = 0; i < timeTableList.size(); i++) {
            int occupied = timeTableList.get(i).getCurrentBookings();
            if (occupied > 0) {
                Map<String, Object> occupiedSlot = new HashMap<>();
                occupiedSlot.put("time", timeTableList.get(i).getTime().toString());
                occupiedSlot.put("count", occupied);
                occupiedTime.add(occupiedSlot);
            }
        }

        return occupiedTime;
    }

    @Transactional
    public void generateTimeTable(Long poolId, LocalDate startDate, LocalDate endDate) {
        Pool pool = poolRepository.findById(poolId).
                orElseThrow(() -> new EntityNotFoundException("Запись не найдена в БД"));
        List<WorkSchedule> workScheduleList = workScheduleRepository.findByPool(pool);
        List<HolidayWorkSchedule> holidayWorkScheduleList = holidayWorkScheduleRepository.findAll();
        List<TimeTable> slots = new ArrayList<>();

        Map<DayOfWeek, WorkSchedule> scheduleMap = new HashMap<>();
        for (int i = 0; i < workScheduleList.size(); i++) {
            scheduleMap.put(
                    workScheduleList.get(i).getDayOfWeek(),
                    workScheduleList.get(i)
            );
        }

        Map<LocalDate, HolidayWorkSchedule> holidayWorkScheduleMap = new HashMap<>();
        for (int i = 0; i < holidayWorkScheduleList.size(); i++) {
            holidayWorkScheduleMap.put(
                    holidayWorkScheduleList.get(i).getDate(),
                    holidayWorkScheduleList.get(i)
            );
        }

        for (LocalDate date = startDate; date.isAfter(endDate) == false; date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            WorkSchedule schedule = scheduleMap.get(dayOfWeek);
            if (schedule == null) {
                throw new IllegalStateException("Нет расписания для дня: " + dayOfWeek);
            }

            if (schedule.isDayOf() == true) {
                continue;
            }

            generateTableForDay(pool, date, schedule, slots);
        }
        timeTableRepository.saveAll(slots);
    }

    @Transactional
    public void generateTableForDay(Pool pool, LocalDate date, WorkSchedule workSchedule, List<TimeTable> timeTableList) {
        HolidayWorkSchedule holiday = holidayWorkScheduleRepository.findByDate(date);

        LocalTime openTime = workSchedule.getOpenTime();
        LocalTime closeTime = workSchedule.getCloseTime();

        if (holiday != null && holiday.getOpenTime() != null && holiday.getCloseTime() != null) {
            openTime = holiday.getOpenTime();
            closeTime = holiday.getCloseTime();
        }

        for (LocalTime currentTime = openTime;
             currentTime.isBefore(closeTime);
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

    @Transactional
    public void updateTimeTableForHoliday(LocalDate date, LocalTime openTime, LocalTime closeTime) {
        deleteTimeTableForDate(date);

        List<Pool> pools = poolRepository.findAll();

        for (int i = 0; i < pools.size(); i++) {
            WorkSchedule workSchedule = workScheduleRepository.findByPoolIdAndDayOfWeek(pools.get(i).getId(), date.getDayOfWeek());

            if (workSchedule == null) {
                throw new IllegalStateException("Расписание работы не найдено");
            }

            WorkSchedule holidaySchedule = new WorkSchedule();
            holidaySchedule.setOpenTime(openTime);
            holidaySchedule.setCloseTime(closeTime);

            List<TimeTable> timeTables = new ArrayList<>();
            generateTableForDay(pools.get(i), date, holidaySchedule, timeTables);

            timeTableRepository.saveAll(timeTables);
        }
    }

    @Transactional
    public void deleteTimeTableForDate(LocalDate date) {
        timeTableRepository.deleteByDate(date);
    }

    @Transactional
    public void generateTimeTableForDate(LocalDate date) {
        List<Pool> pools = poolRepository.findAll();
        for (int i = 0; i < pools.size(); i++) {
            WorkSchedule workSchedule = workScheduleRepository.findByPoolIdAndDayOfWeek(pools.get(i).getId(), date.getDayOfWeek());

            if (workSchedule == null) {
                throw new IllegalStateException("Расписание работы не найдено");
            }

            List<TimeTable> timeTables = new ArrayList<>();
            generateTableForDay(pools.get(i), date, workSchedule, timeTables);
            timeTableRepository.saveAll(timeTables);
        }
    }

    @Transactional
    public void regenerateTimeTableForHoliday(LocalDate oldDate, LocalDate newDate) {
        if (!oldDate.equals(newDate)) {
            timeTableRepository.deleteByDate(oldDate);
            generateTimeTableForDate(oldDate);
        }

        timeTableRepository.deleteByDate(newDate);
        generateTimeTableForDate(newDate);
    }
}

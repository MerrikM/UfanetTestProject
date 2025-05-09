package com.pool.poolcrud.Service;

import com.pool.poolcrud.DTO.HolydayWorkSchedule.HolidayWorkScheduleDTO;
import com.pool.poolcrud.Model.HolidayWorkSchedule;
import com.pool.poolcrud.Repository.HolidayWorkScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class HolidayWorkScheduleService {
    private final HolidayWorkScheduleRepository holidayWorkScheduleRepository;
    private final TimeTableService timeTableService;

    public HolidayWorkScheduleService(HolidayWorkScheduleRepository holidayWorkScheduleRepository, TimeTableService timeTableService) {
        this.holidayWorkScheduleRepository = holidayWorkScheduleRepository;
        this.timeTableService = timeTableService;
    }

    @Transactional(readOnly = true)
    public HolidayWorkSchedule getHolidayWorkScheduleById(Long id) {
        return holidayWorkScheduleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Запись не найдена в БД")
        );
    }

    @Transactional(readOnly = true)
    public List<HolidayWorkSchedule> findAll() {
        return holidayWorkScheduleRepository.findAll();
    }

    @Transactional
    public void addHoliday(LocalDate date, String description,
                           LocalTime openTime, LocalTime closeTime) {
        HolidayWorkSchedule holiday = new HolidayWorkSchedule();
        holiday.setDate(date);
        holiday.setDescription(description);
        holiday.setOpenTime(openTime);
        holiday.setCloseTime(closeTime);
        holidayWorkScheduleRepository.save(holiday);

        timeTableService.updateTimeTableForHoliday(date, openTime, closeTime);
    }

    @Transactional
    public void updateHoliday(Long id, HolidayWorkScheduleDTO holidayWorkScheduleDTO) {

        HolidayWorkSchedule holiday = getHolidayWorkScheduleById(id);
        LocalDate oldDate = holiday.getDate();

        holiday.setDate(holidayWorkScheduleDTO.getDate());
        holiday.setDescription(holidayWorkScheduleDTO.getDescription());
        holiday.setOpenTime(holidayWorkScheduleDTO.getOpenTime());
        holiday.setCloseTime(holidayWorkScheduleDTO.getCloseTime());

        holidayWorkScheduleRepository.save(holiday);

        timeTableService.regenerateTimeTableForHoliday(oldDate, holiday.getDate());
    }

    @Transactional
    public void removeHoliday(LocalDate date) {
        holidayWorkScheduleRepository.deleteByDate(date);
    }

    public HolidayWorkSchedule isHoliday(LocalDate date) {
        return holidayWorkScheduleRepository.findByDate(date);
    }
}

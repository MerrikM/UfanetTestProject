package com.pool.poolcrud.Service;

import com.pool.poolcrud.DTO.PoolDTO;
import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Model.TimeTable;
import com.pool.poolcrud.Model.WorkSchedule;
import com.pool.poolcrud.Repository.PoolRepository;
import com.pool.poolcrud.Repository.TimeTableRepository;
import com.pool.poolcrud.Repository.WorkScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
public class PoolService {

    private final PoolRepository poolRepository;
    private final TimeTableRepository timeTableRepository;
    private final WorkScheduleService workScheduleService;
    private final TimeTableService timeTableService;

    public PoolService(PoolRepository poolRepository, TimeTableRepository timeTableRepository, WorkScheduleService workScheduleService, TimeTableService timeTableService) {
        this.poolRepository = poolRepository;
        this.timeTableRepository = timeTableRepository;
        this.workScheduleService = workScheduleService;
        this.timeTableService = timeTableService;
    }

    @Transactional(readOnly = true)
    public Pool getPoolById(Long id) {
        return poolRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Бассейн не найден в БД")
        );
    }

    @Transactional(readOnly = true)
    public List<Pool> getPools() {
        return poolRepository.findAll();
    }

    @Transactional
    public Pool createPool(PoolDTO newPool) {

        Pool pool = new Pool();
        pool.setName(newPool.getName());
        pool.setMaxCapacity(newPool.getMaxCapacity());
        pool.setMaxVisitsPerDay(newPool.getMaxVisitsPerDay());
        pool.setTimeTables(Collections.emptyList());

        poolRepository.save(pool);

        workScheduleService.setupDefaultWorkSchedule(pool);

        timeTableService.generateTimeTable(pool.getId(), LocalDate.now(), LocalDate.now().plusMonths(1));


        return pool;
    }

    @Transactional
    public Pool updatePool(Long id, PoolDTO poolWhoBeUpdated) {
        Pool pool = getPoolById(id);

        pool.setName(poolWhoBeUpdated.getName());
        pool.setMaxCapacity(poolWhoBeUpdated.getMaxCapacity());

        poolRepository.save(pool);
        return pool;
    }

    @Transactional
    public void deletePool(Long id) {
        poolRepository.deleteById(id);
    }
}

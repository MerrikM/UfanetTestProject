package com.pool.poolcrud.Service;

import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Model.TimeTable;
import com.pool.poolcrud.Model.WorkSchedule;
import com.pool.poolcrud.Repository.PoolRepository;
import com.pool.poolcrud.Repository.TimeTableRepository;
import com.pool.poolcrud.Repository.WorkScheduleRepository;
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
    private final WorkScheduleRepository workScheduleRepository;
    private final WorkScheduleService workScheduleService;
    private final TimeTableService timeTableService;

    public PoolService(PoolRepository poolRepository, TimeTableRepository timeTableRepository, WorkScheduleRepository workScheduleRepository, WorkScheduleService workScheduleService, TimeTableService timeTableService) {
        this.poolRepository = poolRepository;
        this.timeTableRepository = timeTableRepository;
        this.workScheduleRepository = workScheduleRepository;
        this.workScheduleService = workScheduleService;
        this.timeTableService = timeTableService;
    }

    @Transactional(readOnly = true)
    public Pool getPoolById(Long id) {
        return poolRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Бассейн не найден в БД")
        );
    }

    @Transactional(readOnly = true)
    public List<Pool> getPools() {
        return poolRepository.findAll();
    }

    @Transactional
    public Pool createPool(Pool newPool, int maxCapacity) {

        Pool pool = new Pool();
        pool.setName(newPool.getName());
        pool.setMaxCapacity(maxCapacity);
        pool.setTimeTables(Collections.emptyList());

        poolRepository.save(pool);

        workScheduleService.setupDefaultWorkSchedule(pool);

        timeTableService.generateTimeTable(pool.getId(), LocalDate.now(), LocalDate.now().plusMonths(1));

//        WorkSchedule workSchedule = new WorkSchedule();
//        workSchedule.setPool(pool);
//        workSchedule.setOpenTime(openTime);
//        workSchedule.setCloseTime(closeTime);
//        workSchedule.setHoliday(false);
//
//        workScheduleRepository.save(workSchedule);
        return pool;
    }

    @Transactional
    public Pool updatePool(Long id, Pool poolWhoBeUpdated) {
        Pool pool = getPoolById(id);
        if (pool.getId() == null) {
            System.out.println("Бассейн не найден в БД");
            return null;
        }
        pool.setName(poolWhoBeUpdated.getName());
        pool.setMaxCapacity(poolWhoBeUpdated.getMaxCapacity());

        poolRepository.save(pool);
        return pool;
    }

    @Transactional
    public void deletePool(Long id) {
        poolRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TimeTable> getAvailableTime(Long id) {
        Pool pool = getPoolById(id);
        if (pool.getId() == null) {
            System.out.println("Бассейн не найден в БД");
            return null;
        }

        List<TimeTable> timeTables = timeTableRepository.findByPoolIdAndCurrentBookingsLessThan(
                id,
                pool.getMaxCapacity()
        );

        return timeTables;
    }
}

package com.pool.poolcrud.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Service.PoolService;
import com.pool.poolcrud.Service.TimeTableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v0/pool/")
public class PoolController {

    private final PoolService poolService;
    private final TimeTableService timeTableService;

    public PoolController(PoolService poolService, TimeTableService timeTableService) {
        this.poolService = poolService;
        this.timeTableService = timeTableService;
    }

    @PostMapping("/create")
    public ResponseEntity<Pool> createPool(@RequestBody Pool pool) throws JsonProcessingException {
        Pool newPool = new Pool();
        newPool.setName(pool.getName());

        Pool createdPool = poolService.createPool(
                newPool,
                pool.getMaxCapacity()
        );

        return ResponseEntity.ok(createdPool);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<Pool> updatePool(@PathVariable("id") Long id, @RequestBody Pool pool) {
        Pool updatedPool = poolService.updatePool(id, pool);

        if (updatedPool == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedPool);
    }

    @GetMapping("/{id}/timetable/available")
    public ResponseEntity<List<Map<String, Object>>> getAvailable(@PathVariable("id") Long poolId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Map<String, Object>> slots = timeTableService.getAvailable(poolId, localDate);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/{id}/timetable/all")
    public ResponseEntity<List<Map<String, Object>>> getOccupied(@PathVariable("id") Long poolId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Map<String, Object>> slots = timeTableService.getOccupied(poolId, localDate);
        return ResponseEntity.ok(slots);
    }

}

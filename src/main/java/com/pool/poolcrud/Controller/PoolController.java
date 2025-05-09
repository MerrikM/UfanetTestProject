package com.pool.poolcrud.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pool.poolcrud.DTO.Pool.PoolCreateRequest;
import com.pool.poolcrud.DTO.PoolDTO;
import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Service.PoolService;
import com.pool.poolcrud.Service.TimeTableService;
import jakarta.persistence.EntityNotFoundException;
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

    @GetMapping("{id}/get")
    public ResponseEntity<?> getPool(@PathVariable("id") Long id) {
        try {
            Pool pool = poolService.getPoolById(id);
            return ResponseEntity.ok(pool);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPools() {
        List<Pool> pools = poolService.getPools();
        return ResponseEntity.ok(pools);
    }

    @PostMapping("/create")
    public ResponseEntity<Pool> createPool(@RequestBody PoolDTO pool) {
        return ResponseEntity.ok(poolService.createPool(pool));
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<?> updatePool(@PathVariable("id") Long id, @RequestBody PoolDTO pool) {
        try {
            Pool updatedPool = poolService.updatePool(id, pool);

            if (updatedPool == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedPool);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/timetable/available")
    public ResponseEntity<List<Map<String, Object>>> getAvailable(@PathVariable("id") Long poolId, @RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<Map<String, Object>> slots = timeTableService.getAvailable(poolId, localDate);
            return ResponseEntity.ok(slots);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/timetable/all")
    public ResponseEntity<List<Map<String, Object>>> getOccupied(@PathVariable("id") Long poolId, @RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<Map<String, Object>> slots = timeTableService.getOccupied(poolId, localDate);
            return ResponseEntity.ok(slots);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        poolService.deletePool(id);
        return ResponseEntity.ok().build();
    }
}

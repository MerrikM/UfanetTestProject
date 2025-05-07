package com.pool.poolcrud.Repository;

import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoolRepository extends JpaRepository<Pool, Long> {
}

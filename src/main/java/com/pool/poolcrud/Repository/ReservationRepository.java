package com.pool.poolcrud.Repository;

import com.pool.poolcrud.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByOrderId(String orderId);

    Reservation findByOrderId(String orderId);

}

package com.pool.poolcrud.Repository;

import com.pool.poolcrud.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByOrderId(String orderId);

    Reservation findByOrderId(String orderId);

    List<Reservation> findByClient_NameAndClient_SurnameAndClient_Patronymic(String clientName, String clientSurname, String clientPatronymic);

    List<Reservation> findByTimeTable_Date(LocalDate timeTableDate);
}

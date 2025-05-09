package com.pool.poolcrud.Repository;

import com.pool.poolcrud.Model.Client;
import com.pool.poolcrud.Model.Reservation;
import com.pool.poolcrud.Model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findByOrderId(String orderId);

    List<Reservation> findByClient_NameAndClient_SurnameAndClient_Patronymic(String clientName, String clientSurname, String clientPatronymic);

    List<Reservation> findByTimeTable_DateAndClient_Id(LocalDate timeTableDate, Long clientId);

    int countByClientAndTimeTable_Date(Client client, LocalDate timeTableDate);

    boolean existsByClientAndTimeTable(Client client, TimeTable timeTable);
}
